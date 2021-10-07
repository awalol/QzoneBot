package cn.awalol.qzoneBot

import cn.awalol.qzoneBot.bean.qqMusic.SongInfo
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.net.URLEncoder

/**
 * @author awalo
 * @date 2021/3/7
 */

/**
 * QQ音乐API集合
 */
internal object MusicApi {
    private val client = HttpClient(CIO)
    private val objectMapper = ObjectMapper()

    /**
     * 获取歌曲详细信息
     * @param mid 歌曲mid
     */
    suspend fun qqMusicSongInfo(mid: String): SongInfo {
        val stringResponse : String = client.post{
            url("https://c.y.qq.com/v8/fcg-bin/fcg_play_single_song.fcg?")
            body = "songmid=$mid&format=json"
        }
        println(stringResponse)
        return objectMapper.readValue(stringResponse, SongInfo::class.java)
    }

    /**
     * 搜索歌曲
     * @param content 搜索内容
     */
    suspend fun qqMusicSearch(content : String): List<JsonNode> {
        val stringResponse : String = client.get("https://c.y.qq.com/soso/fcgi-bin/client_search_cp?format=json&n=20&p=0&w=${URLEncoder.encode(content,"UTF-8")}&cr=1&g_tk=5381&t=0"){
            headers{
                append(HttpHeaders.Referrer,"https://y.qq.com")
            }
        }
        val jsonNode = objectMapper.readTree(stringResponse)
        return jsonNode["data"]["song"]["list"].toList()
    }
}