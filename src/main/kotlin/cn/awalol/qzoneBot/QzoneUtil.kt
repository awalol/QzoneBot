package cn.awalol.qzoneBot

import cn.awalol.qzoneBot.bean.qzoneSuosuo.PicInfo
import cn.awalol.qzoneBot.bean.qzoneSuosuo.PicinfoX
import cn.awalol.qzoneBot.bean.qzoneSuosuo.UploadPic
import cn.awalol.qzoneBot.objectMapper
import cn.awalol.qzoneBot.qzoneCookie
import org.apache.http.HttpEntity
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import java.util.*
import kotlin.collections.ArrayList

object QzoneUtil {
    fun getGtk(sKey: String): Long {
        var hash: Long = 5381
        for (element in sKey) {
            hash += (hash shl 5) + element.toLong()
        }
        return hash and 0x7fffffff
    }

    fun login(){
        val driver = ChromeDriver()
        driver.manage().window().size = Dimension(305,420);
        driver.get("https://xui.ptlogin2.qq.com/cgi-bin/xlogin?proxy_url=https%3A//qzs.qq.com/qzone/v6/portal/proxy.html&daid=5&&hide_title_bar=1&low_login=0&qlogin_auto_login=1&no_verifyimg=1&link_target=blank&appid=549000912&style=22&target=self&s_url=https%3A%2F%2Fqzs.qzone.qq.com%2Fqzone%2Fv5%2Floginsucc.html%3Fpara%3Dizone&pt_qr_app=%E6%89%8B%E6%9C%BAQQ%E7%A9%BA%E9%97%B4&pt_no_auth=1")
        try {
            while (true){
                if(driver.currentUrl.contains("https://user.qzone.qq.com/")){
                    driver.manage().cookies.forEach { cookie ->
                        if(cookie.name.contentEquals("p_uin").or(cookie.name.contentEquals("p_skey"))){
                            qzoneCookie.put(if (cookie.name.contentEquals("p_uin")) "p_uin" else "p_skey",cookie.value)
                        }
                    }
                    return
                }
            }
        }finally {
            driver.quit()
        }
    }

    fun sendSuosuo(content : String) : String{
        val client : CloseableHttpClient = HttpClients.createDefault()
        val httpPost : HttpPost = HttpPost("https://mobile.qzone.qq.com/mood/publish_mood?g_tk=" + getGtk(
            qzoneCookie.getValue("p_skey"))
        )
        httpPost.setHeader("cookie","p_uin=" + qzoneCookie.getValue("p_uin") + ";p_skey=" + qzoneCookie.getValue("p_skey") + ";")
//        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8")
        val params = ArrayList<NameValuePair>()
        params.add(BasicNameValuePair("opr_type","publish_shuoshuo"))
        params.add(BasicNameValuePair("content",content))
        params.add(BasicNameValuePair("format","json"))
        httpPost.entity = UrlEncodedFormEntity(params)
        val response: CloseableHttpResponse = client.execute(httpPost)
        val responseEntity: HttpEntity = response.getEntity()
        client.close()
        return EntityUtils.toString(responseEntity)
    }

    fun sendSuosuo(content : String,image : String) : String{
        //Create HttpClient
        val client : CloseableHttpClient = HttpClients.createDefault()
        //encode Image
        var imageBase64: String?;
        val httpEntity_encodeimage : HttpEntity = client.execute(HttpGet(image)).entity
        imageBase64 = Base64.getUrlEncoder().encodeToString(EntityUtils.toByteArray(httpEntity_encodeimage))
        //upload Image to Qzone
        val httpPost_upload = HttpPost("https://mobile.qzone.qq.com/up/cgi-bin/upload/cgi_upload_pic_v2?g_tk=".plus(
            getGtk(qzoneCookie.getValue("p_skey"))))
        httpPost_upload.setHeader("cookie","p_uin=".plus(qzoneCookie.getValue("p_uin")).plus(";p_skey=").plus(qzoneCookie.getValue("p_skey") + ";"))
        httpPost_upload.setHeader("Content-Type","application/x-www-form-urlencoded")
        val params_upload = ArrayList<NameValuePair>()
        params_upload.add(BasicNameValuePair("picture",imageBase64))
        params_upload.add(BasicNameValuePair("output_type","json"))
        params_upload.add(BasicNameValuePair("preupload","1"))
        params_upload.add(BasicNameValuePair("base64","1"))
        httpPost_upload.entity = UrlEncodedFormEntity(params_upload)
        val httpEntity_upload : HttpEntity = client.execute(httpPost_upload).entity
        var test1 = toString(httpEntity_upload)
        val uploadPic : UploadPic = objectMapper.readValue(getStringMiddleContent(test1,"_Callback(",");"),
            UploadPic::class.java)
        //upload Image to ablum
        params_upload.clear()
        params_upload.add(BasicNameValuePair("output_type","json"))
        params_upload.add(BasicNameValuePair("preupload","2"))
        params_upload.add(BasicNameValuePair("md5",uploadPic.filemd5))
        params_upload.add(BasicNameValuePair("filelen",uploadPic.filelen.toString()))
        params_upload.add(BasicNameValuePair("refer","shuoshuo"))
        params_upload.add(BasicNameValuePair("albumtype","7"))
        httpPost_upload.entity = UrlEncodedFormEntity(params_upload)
        val httpEntity_image = client.execute(httpPost_upload).entity
        val test = getStringMiddleContent(toString(httpEntity_image),"_Callback([","]);")
        val picInfo : PicinfoX = objectMapper.readValue(test, PicInfo::class.java).picinfo
        client.close()

        //sendSuosuo
        val client2 : CloseableHttpClient = HttpClients.createDefault()
        val httpPost_suosuo = HttpPost("https://mobile.qzone.qq.com/mood/publish_mood?g_tk=" + getGtk(qzoneCookie.getValue("p_skey")))
        httpPost_suosuo.setHeader("cookie","p_uin=" + qzoneCookie.getValue("p_uin") + ";p_skey=" + qzoneCookie.getValue("p_skey") + ";")
        val params_suosuo = ArrayList<NameValuePair>()
        params_suosuo.add(BasicNameValuePair("opr_type","publish_shuoshuo"))
        params_suosuo.add(BasicNameValuePair("content",content))
        params_suosuo.add(BasicNameValuePair("format","json"))
        params_suosuo.add(BasicNameValuePair("richval",picInfo.albumid + "," + picInfo.sloc + "," + picInfo.lloc + ",," + picInfo.height + "," + picInfo.width + ",,,"))
        httpPost_suosuo.entity = UrlEncodedFormEntity(params_suosuo,"UTF-8")
        val httpResponse_suosuo = client2.execute(httpPost_suosuo)
        val httpEntity_suosuo = httpResponse_suosuo.entity
        client2.close()
        return toString(httpEntity_suosuo)
    }

    fun getStringMiddleContent (string: String,startString: String,endString: String) : String{
        val startIndex = string.indexOf(startString)
        val endIndex = string.indexOf(endString,startIndex)
        return string.substring(startIndex + startString.length,endIndex)
    }

    fun toString(entity : HttpEntity) : String{
        return EntityUtils.toString(entity)
    }
}