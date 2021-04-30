package cn.awalol.qzoneBot.bean.qqMusic


import com.fasterxml.jackson.annotation.JsonProperty

data class Mv(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("vid")
    val vid: String,
    @JsonProperty("vt")
    val vt: Int
)