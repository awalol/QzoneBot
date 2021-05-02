package cn.awalol.qzoneBot

import cn.awalol.qzoneBot.bean.qqMusic.Singer
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
            url("http://c.y.qq.com/v8/fcg-bin/fcg_play_single_song.fcg?")
            body = "songmid=$mid&format=json"
        }
        println(stringResponse)
        return objectMapper.readValue(stringResponse, SongInfo::class.java)
    }

    fun getSingers(singers : List<Singer>): String{
        val singerList = StringBuffer()
        for(i in 0 until singers.lastIndex){
            singerList.append(singers.get(i).title.plus("/"))
        }
        singerList.append(singers.last().title)
        return singerList.toString()
    }

    suspend fun qqMusicSearch(songName : String, songSinger : String): ListItem? {
        val stringResponse : String = client.get("https://c.y.qq.com/soso/fcgi-bin/search_for_qq_cp?format=json&n=20&p=0&w=${URLEncoder.encode("$songName $songSinger")}&cr=1&g_tk=5381&t=0"){
            headers{
                append(HttpHeaders.Referrer,"https://y.qq.com")
            }
        }
        val jsonMapper = objectMapper.readValue(stringResponse,Search::class.java)
        return jsonMapper!!.data!!.song!!.list!![0]
    }
}