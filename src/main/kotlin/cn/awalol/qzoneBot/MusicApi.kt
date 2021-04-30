package cn.awalol.qzoneBot

import cn.awalol.qzoneBot.bean.qqMusic.Singer
import cn.awalol.qzoneBot.bean.qqMusic.SongInfo
import cn.awalol.qzoneBot.bean.qqMusic.search.ListItem
import cn.awalol.qzoneBot.bean.qqMusic.search.Search
import cn.awalol.qzoneBot.objectMapper
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import java.net.URLEncoder
import java.util.ArrayList

/**
 * @author awalo
 * @date 2021/3/7
 */
object MusicApi {
    private val httpClient : CloseableHttpClient = HttpClients.createDefault()

    fun qqMusic_songInfo(mid: String): SongInfo {
        val httpPost = HttpPost("http://c.y.qq.com/v8/fcg-bin/fcg_play_single_song.fcg?")
        val params: ArrayList<NameValuePair> = ArrayList()
        params.add(BasicNameValuePair("songmid", mid))
        params.add(BasicNameValuePair("format", "json"))
        httpPost.entity = UrlEncodedFormEntity(params)
        val content = QzoneUtil.toString(httpClient.execute(httpPost).entity)
        return objectMapper.readValue(content, SongInfo::class.java)
    }

    fun getSingers(singers : List<Singer>): String{
        val singerList = StringBuffer()
        for(i in 0 until singers.lastIndex){
            singerList.append(singers.get(i).title.plus("/"))
        }
        singerList.append(singers.last().title)
        return singerList.toString()
    }

    fun qqMusic_search(songName : String,songSinger : String): ListItem? {
        val httpGet = HttpGet("https://c.y.qq.com/soso/fcgi-bin/search_for_qq_cp?format=json&n=20&p=0&w=" + URLEncoder.encode("$songName $songSinger") + "&cr=1&g_tk=5381&t=0")
        httpGet.setHeader("Referer","https://y.qq.com")
        val content = QzoneUtil.toString(httpClient.execute(httpGet).entity)
        val jsonMapper = objectMapper.readValue(content,Search::class.java)
        return jsonMapper!!.data!!.song!!.list!![0]
    }
}