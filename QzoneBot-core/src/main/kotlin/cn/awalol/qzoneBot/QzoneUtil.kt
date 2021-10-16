package cn.awalol.qzoneBot

import cn.awalol.qzoneBot.bean.qzoneSuosuo.PicInfo
import cn.awalol.qzoneBot.bean.qzoneSuosuo.PicinfoX
import cn.awalol.qzoneBot.bean.qzoneSuosuo.UploadPic
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import java.net.URLEncoder
import java.util.*
import kotlin.collections.HashMap

internal class QzoneUtil(private val qzoneCookie: HashMap<String, String>) {
    private val userAgent =
        "Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like" +
                " Gecko)" +
                " Chrome/90.0.4430.93 Mobile Safari/537.36"
    private val client = HttpClient(CIO)
    private val objectMapper = ObjectMapper()

    /**
     * QQGTK转换算法
     * @param sKey Cookie中的p_skey
     */
    private fun getGtk(sKey: String): Long {
        var hash: Long = 5381
        for (element in sKey) {
            hash += (hash shl 5) + element.code.toLong()
        }
        return hash and 0x7fffffff
    }

    /**
     * 发布说说
     * @param content 说说内容
     * @param image 附带的图片
     */
    suspend fun publishShuoshuo(content: String, image: String): String {
        //get image ByteArray
        val imageResponse: ByteArray = client.get(image)
        val imageBase64: String = Base64.getUrlEncoder().encodeToString(imageResponse)
        //upload Image to Qzone
        val uploadPic1Response: String = client.post {
            url("https://mobile.qzone.qq.com/up/cgi-bin/upload/cgi_upload_pic_v2?g_tk=" + getGtk(qzoneCookie.getValue("p_skey")))
            headers {
                append(
                    HttpHeaders.Cookie,
                    "p_uin=${qzoneCookie.getValue("p_uin")}; " + "p_skey=${qzoneCookie.getValue("p_skey")};"
                )
                append(HttpHeaders.ContentType, "application/x-www-form-urlencoded")
                append(HttpHeaders.UserAgent, userAgent)
            }
            body = "picture=$imageBase64&output_type=json&preupload=1&base64=1&hd_quality=90"
        }
        val response = uploadPic1Response.getMiddleContent("_Callback(", ");")
        println(response)
        val uploadPic = objectMapper.readValue(response, UploadPic::class.java)

        delay(1000)

        //upload Image to ablum
        val uploadPic2Response: String = client.post {
            url("https://mobile.qzone.qq.com/up/cgi-bin/upload/cgi_upload_pic_v2?g_tk=" + getGtk(qzoneCookie.getValue("p_skey")))
            headers {
                append(
                    HttpHeaders.Cookie,
                    "p_uin=${qzoneCookie.getValue("p_uin")}; " +
                            "p_skey=${qzoneCookie.getValue("p_skey")};"
                )
                append(HttpHeaders.ContentType, "application/x-www-form-urlencoded")
                append(HttpHeaders.UserAgent, userAgent)
            }
            body =
                "output_type=json&preupload=2&md5=${uploadPic.filemd5}&filelen=${uploadPic.filelen}&refer=shuoshuo&albumtype=7"
        }

        val imageContent = uploadPic2Response.getMiddleContent("_Callback([", "]);")
        println(imageContent)
        val picInfo: PicinfoX = objectMapper.readValue(imageContent, PicInfo::class.java).picinfo

        //publish Shuoshuo
        val publishResponse: String = client.post {
            url("https://mobile.qzone.qq.com/mood/publish_mood?g_tk=" + getGtk(qzoneCookie.getValue("p_skey")))
            headers {
                append(
                    HttpHeaders.Cookie,
                    "p_uin=${qzoneCookie.getValue("p_uin")};" +
                            "p_skey=${qzoneCookie.getValue("p_skey")};"
                )
                append(HttpHeaders.UserAgent, userAgent)
            }
            body = "opr_type=publish_shuoshuo&content=${URLEncoder.encode(content, "UTF-8")}&format=json&richval=" +
                    (picInfo.albumid + "," + picInfo.sloc + "," + picInfo.lloc + ",," + picInfo.height + "," + picInfo.width + ",,,")
        }
        val publishMap = objectMapper.readValue(publishResponse, Map::class.java) as Map<*, *>

        if (publishMap["code"].toString().contentEquals("0").not()) {
            throw Exception("发送失败 -> $publishResponse")
        }

        return publishResponse
    }

    private fun String.getMiddleContent(startString: String, endString: String): String {
        val startIndex = this.indexOf(startString)
        val endIndex = this.indexOf(endString, startIndex)
        return this.substring(startIndex + startString.length, endIndex)
    }

    /**
     * QQ空间Cookie存活验证
     * 通过获取QQ好友列表判断Cookie是否存活
     */
    suspend fun cookieIsValid(): Boolean {
        //通过获取qq好友列表以判断Cookie是否存活
        val stringResponse: String =
            client.get("https://mobile.qzone.qq.com/friend/mfriend_list?g_tk=${getGtk(qzoneCookie.getValue("p_skey"))}&res_type=normal&format=json") {
                headers {
                    append(HttpHeaders.UserAgent, userAgent)
                    append(HttpHeaders.Cookie, qzoneCookie.toList().joinToString(";") { "${it.first}=${it.second}" })
                }
            }
        val jsonNode = objectMapper.readTree(stringResponse)
        return jsonNode["code"].asInt() == 0
    }
}