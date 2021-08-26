package cn.awalol.qzoneBot

import cn.awalol.qzoneBot.bean.qqMusic.Singer
import cn.awalol.qzoneBot.bean.qqMusic.SongInfo
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.events.StrangerMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.BotConfiguration
import kotlin.system.exitProcess

val qzoneCookie : HashMap<String, String> = HashMap()
val objectMapper = ObjectMapper()
const val template = "#推歌意向征集#\n" +
        "《%s》（%s）"
val singerBlackList = listOf("0011jjK40orUJx","002nXp292LIOGV","0022eAG537I1bg","0039JTTG0s4SCv")
val client = HttpClient(CIO)
lateinit var qqUin : String
val waitToRepublish : HashMap<String,Image?> = HashMap()

suspend fun main(args : Array<String>){
    qqUin = args[0]
    val bot = BotFactory.newBot(qqUin.toLong(), args[1]) {
        protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
        fileBasedDeviceInfo()
        redirectBotLogToDirectory()
//        redirectNetworkLogToDirectory()
    }

    if(System.getProperty("webdriver.chrome.driver").isNullOrEmpty()){
        bot.logger.error("未找到Chromedriver路径，请下载。请使用-Dwebdriver.chrome.driver设置路径")
        exitProcess(0)
    }

    QzoneUtil.login()
    bot.login()

    if(!bot.isOnline){
        bot.logger.error("登陆失败")
        exitProcess(0)
    }

    bot.globalEventChannel().filter { event: Event -> event is FriendMessageEvent || event is StrangerMessageEvent }.subscribeAlways<MessageEvent> {
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
                val songInfo = MusicApi.qqMusicSongInfo((MusicApi.qqMusicSearch(songName, songSinger)[0]["songmid"].asText()))

                try {
                    if (QzoneUtil.cookieIsValid(qzoneCookie)) {
                        push(songInfo,image,bot)

                        //重试发送未发送成功的说说
                        if(waitToRepublish.isNotEmpty()){
                            bot.logger.info("尝试重新发送之前发送失败的说说")
                            val iterator = waitToRepublish.iterator() //https://stackoverflow.com/questions/14673653/why-isnt-this-code-causing-a-concurrentmodificationexception
                            while (iterator.hasNext()){
                                val item = iterator.next()
                                push(MusicApi.qqMusicSongInfo(item.key),item.value,bot)
                                iterator.remove()
                            }
                        }
                    }else{
                        throw Exception("QQ空间登陆失败")
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                    if(e.message?.contentEquals("QQ空间登陆失败") == true){
                        QzoneUtil.login()//尝试重新登陆
                    }
                    if(!waitToRepublish.containsKey(songInfo.data[0].mid)){
                        waitToRepublish[songInfo.data[0].mid] = image
                        bot.logger.error("发送失败，已添加到重试列表")
                    }
                }
            }
        } else if(qzoneCookie.isEmpty()){
            bot.logger.error("QQ空间未登录")
            bot.getFriend(3512311532)!!.sendMessage("[Error] 说说发送失败，QQ空间未登录")
        }
    }
}

suspend fun push(songInfo : SongInfo, image : Image?,bot : Bot){
    val songData = songInfo.data[0]

    //黑名单歌手判断
    songData.singer.forEach { singer: Singer ->
        if (singerBlackList.contains(singer.mid)) {
            bot.logger.error("黑名单歌手:" + singer.title)
            return
        }
    }

    //发送说说
    if (image != null) {
        QzoneUtil.publishShuoshuo(
            template.format(
                songData.title,
                songData.singer.joinToString(separator = "/"){it.name}
            ),
            image.queryUrl()
        )
    } else {
        QzoneUtil.publishShuoshuo(
            template.format(
                songData.title,
                songData.singer.joinToString(separator = "/"){it.name}
            ),
            "https://y.gtimg.cn/music/photo_new/T002R800x800M000%s.jpg".format(songData.album.mid)
        )
    }
}