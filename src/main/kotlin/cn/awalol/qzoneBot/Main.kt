package cn.awalol.qzoneBot

import cn.awalol.qzoneBot.MusicApi
import cn.awalol.qzoneBot.QzoneUtil
import com.fasterxml.jackson.databind.ObjectMapper
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.SingleMessage
import java.util.HashMap
import kotlin.system.exitProcess

val qzoneCookie : HashMap<String, String> = HashMap()
val objectMapper = ObjectMapper()
val template = "#推歌意向征集#\n" +
        "《%s》(%s)"

suspend fun main(args : Array<String>){

    val bot = BotFactory.newBot(args[0].toLong(), args[1]) {
        fileBasedDeviceInfo()
    }

    bot.login()

    if(!bot.isOnline){
        bot.logger.error("登陆失败")
        exitProcess(0)
    }

    if(System.getProperty("webdriver.chrome.driver").isNullOrEmpty()){
        bot.logger.error("未找到Chromedriver路径，请下载。请使用-Dwebdriver.chrome.driver设置路径")
    }else{
        QzoneUtil.login()
    }

    bot.globalEventChannel().subscribeAlways<FriendMessageEvent> {
        val messageChain : List<SingleMessage> = message
        var image : Image? = null
        var message = ""
        messageChain.forEach { singleMessage ->
            if(singleMessage is Image){
                image = Image(singleMessage.imageId)
                println(singleMessage.imageId)
            }else if(singleMessage is PlainText){
                message = singleMessage.content
                println("PlainText " + singleMessage.content)
            }
        }

        if(qzoneCookie.isNotEmpty()) {
            if("((?<=《).+(?=》))".toRegex().containsMatchIn(message) && "(?<=[（|(]).+(?=[）|)])".toRegex().containsMatchIn(message)){
                println("开始")
                val songName = "((?<=《).+(?=》))".toRegex().find(message)!!.value
                val songSinger = "(?<=[（|(]).+(?=[）|)])".toRegex().find(message)!!.value
                val songInfo = MusicApi.qqMusic_songInfo(
                    MusicApi.qqMusic_search(songName,songSinger)!!.songmid!!
                )
                if(image != null){
                    QzoneUtil.sendSuosuo(
                        template.format(songInfo.data[0].title,
                            MusicApi.getSingers(songInfo.data[0].singer)),
                        image!!.queryUrl()
                    )
                }else{
                    QzoneUtil.sendSuosuo(
                        template.format(
                            songInfo.data[0].title,
                            MusicApi.getSingers(songInfo.data[0].singer)),
                        "http://y.gtimg.cn/music/photo_new/T002R800x800M000%s.jpg".format(songInfo.data[0].album.mid)
                    )
                }
            }
        }else{
            bot.logger.error("QQ空间未登录")
            this.bot.getFriend(3512311532)!!.sendMessage("[Error] 说说发送失败，QQ空间未登录")
        }
    }
}