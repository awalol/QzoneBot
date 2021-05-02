package cn.awalol.qzoneBot

import cn.awalol.qzoneBot.bean.qqMusic.Singer
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.events.StrangerMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.BotConfiguration
import kotlin.system.exitProcess

val qzoneCookie : HashMap<String, String> = HashMap()
val objectMapper = ObjectMapper()
val template = "#推歌意向征集#\n" +
        "《%s》（%s）"
val singerBlackList = listOf("0011jjK40orUJx","002nXp292LIOGV","0022eAG537I1bg")
val client = HttpClient(CIO)

suspend fun main(args : Array<String>){
    val bot = BotFactory.newBot(args[0].toLong(), args[1]) {
        protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
        fileBasedDeviceInfo()
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
        push(message,bot,sender)
    }
}

suspend fun push(messageChain : MessageChain,bot : Bot,sender : User){
    var image : Image? = null
    lateinit var message : String
    messageChain.forEach { singleMessage ->
        if(singleMessage is Image){
            image = Image(singleMessage.imageId)
            bot.logger.info(singleMessage.imageId)
        }else if(singleMessage is PlainText){
            message = singleMessage.content
            bot.logger.info("PlainText " + singleMessage.content)
        }
    }

    if(qzoneCookie.isNotEmpty()) {
        if("((?<=《).+(?=》))".toRegex().containsMatchIn(message) && "(?<=[（|(]).+(?=[）|)])".toRegex().containsMatchIn(message)){
            //获取歌曲信息
            val songName = "((?<=《).+(?=》))".toRegex().find(message)!!.value
            val songSinger = "(?<=[（|(]).+(?=[）|)])".toRegex().find(message)!!.value
            bot.logger.info("$songName $songSinger")
            val songInfo = MusicApi.qqMusicSongInfo(
                MusicApi.qqMusicSearch(
                    songName,
                    songSinger
                )!!.songmid!!
            )
            val songData = songInfo.data[0]

            //黑名单歌手判断
            songData.singer.forEach { singer: Singer ->
                if (singerBlackList.contains(singer.mid)) {
                    bot.logger.error(sender.id.toString() + " 尝试推送黑名单歌手:" + singer.title)
                    return
                }
            }

            //发送说说
            if (image != null) {
                val test = QzoneUtil.publishShuoshuo(
                    template.format(
                        songData.title,
                        MusicApi.getSingers(songData.singer)
                    ),
                    image!!.queryUrl()
                )
                println(test)
            } else {
                val test = QzoneUtil.publishShuoshuo(
                    template.format(
                        songData.title,
                        MusicApi.getSingers(songData.singer)
                    ),
                    "http://y.gtimg.cn/music/photo_new/T002R800x800M000%s.jpg".format(songData.album.mid)
                )
                println(test)
            }
        }
    }else{
        bot.logger.error("QQ空间未登录")
        bot.getFriend(3512311532)!!.sendMessage("[Error] 说说发送失败，QQ空间未登录")
    }
}