package cn.awalol.qzoneBot.bean.qqMusic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Data(
    @JsonProperty("album")
    val album: Album,
    @JsonProperty("mid")
    val mid: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("singer")
    val singer: List<Singer>,
    @JsonProperty("title")
    val title: String
)