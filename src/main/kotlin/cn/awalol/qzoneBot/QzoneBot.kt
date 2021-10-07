package cn.awalol.qzoneBot

import cn.awalol.qzoneBot.bean.qqMusic.Data
import cn.awalol.qzoneBot.bean.qqMusic.Singer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.events.OtherClientMessageEvent
import net.mamoe.mirai.event.events.StrangerMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.PlainText
import okhttp3.internal.wait
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.thread

object QzoneBot {
    /**
     * QQ空间说说模板
     */
    private const val template = "#推歌意向征集#\n" +
            "《%s》（%s）"
    private val waitToRepublish : HashMap<String,Image?> = HashMap()

    /**
     * 注册空间事件
     * @param qzoneCookie QQ空间Cookie
     * @param singerBlackList 黑名单歌手列表
    */
    fun initBot(bot : Bot,qzoneCookie : HashMap<String,String>,singerBlackList : List<String>,exception : ((exception : Exception) -> Unit)){
        val qzoneUtil = QzoneUtil(qzoneCookie)

        //启动重发任务事件
        Timer().schedule(
            object:TimerTask(){
                override fun run() {
                    suspend {
                        val iterator = waitToRepublish.iterator()
                        while (iterator.hasNext()){
                            val item = iterator.next()
                            push(MusicApi.qqMusicSongInfo(item.key).data[0],item.value,bot,qzoneUtil,singerBlackList)
                        }
                    }
                } }
            , 10000,
        )

        bot.globalEventChannel().filter { event: Event -> event is FriendMessageEvent ||
                event is StrangerMessageEvent ||
                event is OtherClientMessageEvent
        }.subscribeAlways<MessageEvent> {
            var image : Image? = null
            lateinit var message : String
            this.message.forEach { singleMessage ->
                if(singleMessage is Image){
                    image = Image(singleMessage.imageId)
                    bot.logger.info(singleMessage.imageId)
                }else if(singleMessage is PlainText){
                    message = singleMessage.content
                    bot.logger.info("PlainText " + singleMessage.content)
                }
            }

            if(qzoneCookie.isNotEmpty() && message.isNotEmpty()) {
                //匹配《》与（）
                if("((?<=《).+(?=》))".toRegex().containsMatchIn(message) && "(?<=[（|(]).+(?=[）|)])".toRegex().containsMatchIn(message)){
                    //获取歌曲信息
                    val songName = "((?<=《).+(?=》))".toRegex().findAll(message).first().value
                    val songSinger = "(?<=[（|(]).+(?=[）|)])".toRegex().findAll(message).last().value
                    bot.logger.info("《$songName》 ($songSinger)")
                    val songInfo = MusicApi.qqMusicSongInfo((MusicApi.qqMusicSearch("$songName $songSinger")[0]["songmid"].asText()))

                    //判断重试列表里是否有当前歌曲，如果有则删除
                    if(waitToRepublish.containsKey(songInfo.data[0].mid)){
                        waitToRepublish.remove(songInfo.data[0].mid)
                    }

                    try {
                        if (qzoneUtil.cookieIsValid()) {
                            push(songInfo.data[0],image,bot,qzoneUtil,singerBlackList)
                        }else{
                            throw Exception("QQ空间登陆失败")
                        }
                    }catch (e : Exception){
                        e.printStackTrace()
                        exception(e)
                        if(!waitToRepublish.containsKey(songInfo.data[0].mid)){
                            waitToRepublish[songInfo.data[0].mid] = image
                            bot.logger.error("发送失败，已添加到重试列表")
                        }
                    }
                }
            } else if(qzoneCookie.isEmpty()){
                bot.logger.error("QQ空间未登录")
            }
        }
    }

    /**
     * 发送说说
     */
    private suspend fun push(songData : Data, image : Image?,bot : Bot,qzoneUtil: QzoneUtil,singerBlackList: List<String>){
        //黑名单歌手判断
        songData.singer.forEach { singer: Singer ->
            if (singerBlackList.contains(singer.mid)) {
                bot.logger.error("黑名单歌手:" + singer.title)
                return
            }
        }

        //发送说说
        qzoneUtil.publishShuoshuo(
            template.format(
                songData.title,
                songData.singer.joinToString(separator = "/"){it.name}
            ),
            image?.queryUrl() ?: "https://y.gtimg.cn/music/photo_new/T002R800x800M000${songData.album.mid}.jpg"
        )
    }
}