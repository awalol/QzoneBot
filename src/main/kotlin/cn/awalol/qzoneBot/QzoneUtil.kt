package cn.awalol.qzoneBot

import cn.awalol.qzoneBot.bean.qzoneSuosuo.PicInfo
import cn.awalol.qzoneBot.bean.qzoneSuosuo.PicinfoX
import cn.awalol.qzoneBot.bean.qzoneSuosuo.UploadPic
import io.ktor.client.request.*
import io.ktor.http.*
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import java.net.URLEncoder
import java.util.*
import kotlin.collections.HashMap

const val userAgent = "Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like" +
        " Gecko)" +
        " Chrome/90.0.4430.93 Mobile Safari/537.36"
//自动点击脚本
const val clickScript = "var faces = document.getElementsByClassName(\"face\");\n" +
        "for(i = 0;i < faces.length;i++){\n" +
        "    if(faces[i].getAttribute(\"uin\") == \"%s\"){\n" +
        "        pt.qlogin.imgClick(faces[i])\n" +
        "    }\n" +
        "}"

object QzoneUtil {
    private fun getGtk(sKey: String): Long {
        var hash: Long = 5381
        for (element in sKey) {
            hash += (hash shl 5) + element.code.toLong()
        }
        return hash and 0x7fffffff
    }

    fun login(){
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
                return
            }
        }
    }

    suspend fun publishShuoshuo(content : String, image : String) : String{
        //get image ByteArray
        val imageResponse : ByteArray = client.get(image)
        val imageBase64: String = Base64.getUrlEncoder().encodeToString(imageResponse)
        //upload Image to Qzone
        val uploadPic1Response : String = client.post{
            url("https://mobile.qzone.qq.com/up/cgi-bin/upload/cgi_upload_pic_v2?g_tk=" + getGtk(qzoneCookie.getValue("p_skey")))
            headers{
                append(HttpHeaders.Cookie,
                    "p_uin=${qzoneCookie.getValue("p_uin")}; " +
                        "p_skey=${qzoneCookie.getValue("p_skey")};"
                )
                append(HttpHeaders.ContentType,"application/x-www-form-urlencoded")
                append(HttpHeaders.UserAgent, userAgent)
            }
            body = "picture=$imageBase64&output_type=json&preupload=1&base64=1&hd_quality=90"
        }
        val response = uploadPic1Response.getMiddleContent("_Callback(",");")
        println(response)
        val uploadPic = objectMapper.readValue(response,UploadPic::class.java)

        Thread.sleep(1000)

        //upload Image to ablum
        val uploadPic2Response : String = client.post{
            url("https://mobile.qzone.qq.com/up/cgi-bin/upload/cgi_upload_pic_v2?g_tk=" + getGtk(qzoneCookie.getValue("p_skey")))
            headers{
                append(HttpHeaders.Cookie,
                    "p_uin=${qzoneCookie.getValue("p_uin")}; " +
                        "p_skey=${qzoneCookie.getValue("p_skey")};"
                )
                append(HttpHeaders.ContentType,"application/x-www-form-urlencoded")
                append(HttpHeaders.UserAgent,"Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Mobile Safari/537.36")
            }
            body = "output_type=json&preupload=2&md5=${uploadPic.filemd5}&filelen=${uploadPic.filelen}&refer=shuoshuo&albumtype=7"
        }

        val imageContent = uploadPic2Response.getMiddleContent("_Callback([","]);")
        println(imageContent)
        val picInfo : PicinfoX = objectMapper.readValue(imageContent, PicInfo::class.java).picinfo

        //publish Shuoshuo
        val publishResponse : String = client.post{
            url("https://mobile.qzone.qq.com/mood/publish_mood?g_tk=" + getGtk(qzoneCookie.getValue("p_skey")))
            headers {
                append(
                    HttpHeaders.Cookie,
                    "p_uin=${qzoneCookie.getValue("p_uin")};" +
                            "p_skey=${qzoneCookie.getValue("p_skey")};"
                )
                append(HttpHeaders.UserAgent,"Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Mobile Safari/537.36")
            }
            body = "opr_type=publish_shuoshuo&content=${URLEncoder.encode(content,"UTF-8")}&format=json&richval=" +
                    (picInfo.albumid + "," + picInfo.sloc + "," + picInfo.lloc + ",," + picInfo.height + "," + picInfo.width + ",,,")
        }
        val publishMap = objectMapper.readValue(publishResponse,Map::class.java) as Map<*, *>

        if(publishMap["code"].toString().contentEquals("0").not()){
            throw Exception("发送失败 -> $publishResponse")
        }

        return publishResponse
    }

    private fun String.getMiddleContent (startString: String, endString: String) : String{
        val startIndex = this.indexOf(startString)
        val endIndex = this.indexOf(endString,startIndex)
        return this.substring(startIndex + startString.length,endIndex)
    }

    suspend fun cookieIsValid(cookie : HashMap<String,String>) : Boolean{
        //通过获取qq好友列表以判断Cookie是否存活
        val stringResponse : String = client.get("https://mobile.qzone.qq.com/friend/mfriend_list?g_tk=${getGtk(cookie.getValue("p_skey"))}&res_type=normal&format=json"){
            headers{
                append(HttpHeaders.UserAgent, userAgent)
                append(HttpHeaders.Cookie,cookie.toList().joinToString(";") { "${it.first}=${it.second}" })
            }
        }
        val jsonNode = objectMapper.readTree(stringResponse)
        return jsonNode["code"].asInt() == 0//懒得写JSON反序列化了，直接判断文本是否存在吧，如果出问题再改
    }
}