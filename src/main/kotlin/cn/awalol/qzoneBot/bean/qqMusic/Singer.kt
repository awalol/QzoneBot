package cn.awalol.qzoneBot.bean.qqMusic


import com.fasterxml.jackson.annotation.JsonProperty

data class Singer(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("mid")
    val mid: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("pmid")
    val pmid: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("type")
    val type: Int,
    @JsonProperty("uin")
    val uin: Int
)