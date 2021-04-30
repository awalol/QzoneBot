package cn.awalol.qzoneBot.bean.qqMusic


import com.fasterxml.jackson.annotation.JsonProperty

data class Data(
    @JsonProperty("action")
    val action: Action,
    @JsonProperty("aid")
    val aid: Int,
    @JsonProperty("album")
    val album: Album,
    @JsonProperty("bpm")
    val bpm: Int,
    @JsonProperty("data_type")
    val dataType: Int,
    @JsonProperty("es")
    val es: String,
    @JsonProperty("file")
    val `file`: File,
    @JsonProperty("fnote")
    val fnote: Int,
    @JsonProperty("genre")
    val genre: Int,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("index_album")
    val indexAlbum: Int,
    @JsonProperty("index_cd")
    val indexCd: Int,
    @JsonProperty("interval")
    val interval: Int,
    @JsonProperty("isonly")
    val isonly: Int,
    @JsonProperty("ksong")
    val ksong: Ksong,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("language")
    val language: Int,
    @JsonProperty("mid")
    val mid: String,
    @JsonProperty("modify_stamp")
    val modifyStamp: Int,
    @JsonProperty("mv")
    val mv: Mv,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("ov")
    val ov: Int,
    @JsonProperty("pay")
    val pay: Pay,
    @JsonProperty("sa")
    val sa: Int,
    @JsonProperty("singer")
    val singer: List<Singer>,
    @JsonProperty("status")
    val status: Int,
    @JsonProperty("subtitle")
    val subtitle: String,
    @JsonProperty("tid")
    val tid: Int,
    @JsonProperty("time_public")
    val timePublic: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("trace")
    val trace: String,
    @JsonProperty("type")
    val type: Int,
    @JsonProperty("url")
    val url: String,
    @JsonProperty("version")
    val version: Int,
    @JsonProperty("volume")
    val volume: Volume
)