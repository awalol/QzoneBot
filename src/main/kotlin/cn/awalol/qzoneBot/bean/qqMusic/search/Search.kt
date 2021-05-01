package cn.awalol.qzoneBot.bean.qqMusic.search

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

data class Search(

	@field:JsonProperty("code")
	val code: Int? = null,

	@field:JsonProperty("data")
	val data: Data? = null,

	@field:JsonProperty("subcode")
	val subcode: Int? = null,

	@field:JsonProperty("time")
	val time: Int? = null,

	@field:JsonProperty("message")
	val message: String? = null,

	@field:JsonProperty("tips")
	val tips: String? = null,

	@field:JsonProperty("notice")
	val notice: String? = null
)

data class Semantic(

	@field:JsonProperty("curnum")
	val curnum: Int? = null,

	@field:JsonProperty("curpage")
	val curpage: Int? = null,

	@field:JsonProperty("totalnum")
	val totalnum: Int? = null,

	@field:JsonProperty("list")
	val list: List<Any?>? = null
)

data class Song(

	@field:JsonProperty("curnum")
	val curnum: Int? = null,

	@field:JsonProperty("curpage")
	val curpage: Int? = null,

	@field:JsonProperty("totalnum")
	val totalnum: Int? = null,

	@field:JsonProperty("list")
	val list: List<ListItem?>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SingerItem(

	@field:JsonProperty("name")
	val name: String? = null,

	@field:JsonProperty("name_hilight")
	val nameHilight: String? = null,

	@field:JsonProperty("mid")
	val mid: String? = null,

	@field:JsonProperty("id")
	val id: Int? = null
)

data class Pay(

	@field:JsonProperty("payplay")
	val payplay: Int? = null,

	@field:JsonProperty("payalbum")
	val payalbum: Int? = null,

	@field:JsonProperty("paydownload")
	val paydownload: Int? = null,

	@field:JsonProperty("paytrackmouth")
	val paytrackmouth: Int? = null,

	@field:JsonProperty("paytrackprice")
	val paytrackprice: Int? = null,

	@field:JsonProperty("payalbumprice")
	val payalbumprice: Int? = null,

	@field:JsonProperty("payinfo")
	val payinfo: Int? = null
)

data class Zhida(

	@field:JsonProperty("type")
	val type: Int? = null,

	@field:JsonProperty("chinesesinger")
	val chinesesinger: Int? = null
)

data class Data(

	@field:JsonProperty("song")
	val song: Song? = null,

	@field:JsonProperty("zhida")
	val zhida: Zhida? = null,

	@field:JsonProperty("semantic")
	val semantic: Semantic? = null,

	@field:JsonProperty("qc")
	val qc: List<QcItem?>? = null,

	@field:JsonProperty("totaltime")
	val totaltime: Int? = null,

	@field:JsonProperty("keyword")
	val keyword: String? = null,

	@field:JsonProperty("priority")
	val priority: Int? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ListItem(

	@field:JsonProperty("preview")
	val preview: Preview? = null,

	@field:JsonProperty("songname_hilight")
	val songnameHilight: String? = null,

	@field:JsonProperty("singer")
	val singer: List<SingerItem?>? = null,

	@field:JsonProperty("albumname_hilight")
	val albumnameHilight: String? = null,

	@field:JsonProperty("lyric_hilight")
	val lyricHilight: String? = null,

	@field:JsonProperty("nt")
	val nt: Long? = null,

	@field:JsonProperty("songmid")
	val songmid: String? = null,

	@field:JsonProperty("pure")
	val pure: Int? = null,

	@field:JsonProperty("type")
	val type: Int? = null,

	@field:JsonProperty("chinesesinger")
	val chinesesinger: Int? = null,

	@field:JsonProperty("switch")
	val jsonMemberSwitch: Int? = null,

	@field:JsonProperty("albumname")
	val albumname: String? = null,

	@field:JsonProperty("vid")
	val vid: String? = null,

	@field:JsonProperty("stream")
	val stream: Int? = null,

	@field:JsonProperty("tag")
	val tag: Int? = null,

	@field:JsonProperty("ver")
	val ver: Int? = null,

	@field:JsonProperty("isonly")
	val isonly: Int? = null,

	@field:JsonProperty("grp")
	val grp: List<Any?>? = null,

	@field:JsonProperty("docid")
	val docid: String? = null,

	@field:JsonProperty("albummid")
	val albummid: String? = null,

	@field:JsonProperty("format")
	val format: String? = null,

	@field:JsonProperty("albumid")
	val albumid: Int? = null,

	@field:JsonProperty("msgid")
	val msgid: Int? = null,

	@field:JsonProperty("pay")
	val pay: Pay? = null,

	@field:JsonProperty("size128")
	val size128: Int? = null,

	@field:JsonProperty("sizeflac")
	val sizeflac: Int? = null,

	@field:JsonProperty("sizeogg")
	val sizeogg: Int? = null,

	@field:JsonProperty("songname")
	val songname: String? = null,

	@field:JsonProperty("size320")
	val size320: Int? = null,

	@field:JsonProperty("songurl")
	val songurl: String? = null,

	@field:JsonProperty("t")
	val T: Int? = null,

	@field:JsonProperty("lyric")
	val lyric: String? = null,

	@field:JsonProperty("sizeape")
	val sizeape: Int? = null,

	@field:JsonProperty("pubtime")
	val pubtime: Int? = null,

	@field:JsonProperty("interval")
	val interval: Int? = null,

	@field:JsonProperty("alertid")
	val alertid: Int? = null,

	@field:JsonProperty("songid")
	val songid: Int? = null
)

data class QcItem(

	@field:JsonProperty("text")
	val text: String? = null,

	@field:JsonProperty("type")
	val type: Int? = null
)

data class Preview(

	@field:JsonProperty("tryend")
	val tryend: Int? = null,

	@field:JsonProperty("trybegin")
	val trybegin: Int? = null,

	@field:JsonProperty("trysize")
	val trysize: Int? = null
)
