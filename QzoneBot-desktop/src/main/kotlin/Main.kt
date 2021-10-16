import cn.awalol.qzoneBot.QzoneBot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import org.openqa.selenium.Dimension
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val account = System.getProperty("account",args[0])
        val password = System.getProperty("password",args[1])
        val bot = BotFactory.newBot(account.toLong(),password){
            protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
            fileBasedDeviceInfo()
        }

        QzoneBot.initBot(
            bot = bot,
            qzoneCookie = qzoneLogin(account),
            singerBlackList = listOf("0011jjK40orUJx","002nXp292LIOGV","0022eAG537I1bg","0039JTTG0s4SCv"),
            exception = {
                println(it)
            }
        )

    }

    fun qzoneLogin(qqUin : String) : HashMap<String,String>{
        //自动点击脚本
        val clickScript = "var faces = document.getElementsByClassName(\"face\");\n" +
                "for(i = 0;i < faces.length;i++){\n" +
                "    if(faces[i].getAttribute(\"uin\") == \"%s\"){\n" +
                "        pt.qlogin.imgClick(faces[i])\n" +
                "    }\n" +
                "}"
        val qzoneCookie : HashMap<String,String> = HashMap()

        val provider = ChromeDriver()
        provider.manage().window().size = Dimension(305,420)
        provider.get("https://xui.ptlogin2.qq.com/cgi-bin/xlogin?proxy_url=https%3A//qzs.qq.com/qzone/v6/portal/proxy.html&daid=5&&hide_title_bar=1&low_login=0&qlogin_auto_login=1&no_verifyimg=1&link_target=blank&appid=549000912&style=22&target=self&s_url=https%3A%2F%2Fqzs.qzone.qq.com%2Fqzone%2Fv5%2Floginsucc.html%3Fpara%3Dizone&pt_qr_app=%E6%89%8B%E6%9C%BAQQ%E7%A9%BA%E9%97%B4&pt_no_auth=1")
        provider.executeScript(clickScript.format(qqUin))
        while (true){
            if(provider.currentUrl.contains("https://user.qzone.qq.com/")){
                provider.manage().cookies.forEach { cookie ->
                    qzoneCookie[cookie.name] = cookie.value
                }
                provider.close()
                return qzoneCookie
            }
        }
    }
}