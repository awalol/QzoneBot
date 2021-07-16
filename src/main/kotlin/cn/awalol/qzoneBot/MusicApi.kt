package cn.awalol.qzoneBot

import cn.awalol.qzoneBot.bean.qqMusic.SongInfo
import cn.awalol.qzoneBot.bean.qqMusic.search.ListItem
import cn.awalol.qzoneBot.bean.qqMusic.search.Search
import io.ktor.client.request.*
import io.ktor.http.*
import java.net.URLEncoder

/**
 * @author awalo
 * @date 2021/3/7
 */
object MusicApi {

    suspend fun qqMusicSongInfo(mid: String): SongInfo {
        val stringResponse : String = client.post{
            url("https://c.y.qq.com/v8/fcg-bin/fcg_play_single_song.fcg?")
            body = "songmid=$mid&format=json"
        }
        println(stringResponse)
        return objectMapper.readValue(stringResponse, SongInfo::class.java)
    }

    suspend fun qqMusicSearch(songName : String, songSinger : String): ListItem? {
        val stringResponse : String = client.get("https://c.y.qq.com/soso/fcgi-bin/client_search_cp?format=json&n=20&p=0&w=${URLEncoder.encode("$songName $songSinger","UTF-8")}&cr=1&g_tk=5381&t=0"){
            headers{
                append(HttpHeaders.Referrer,"https://y.qq.com")
            }
        }
        val jsonMapper = objectMapper.readValue(stringResponse,Search::class.java)
        return jsonMapper!!.data!!.song!!.list!![0]
    }
}