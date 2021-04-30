package cn.awalol.qzoneBot.bean.qqMusic


import com.fasterxml.jackson.annotation.JsonProperty

data class Album(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("mid")
    val mid: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("pmid")
    val pmid: String,
    @JsonProperty("subtitle")
    val subtitle: String,
    @JsonProperty("time_public")
    val timePublic: String,
    @JsonProperty("title")
    val title: String
)