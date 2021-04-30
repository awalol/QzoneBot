package cn.awalol.qzoneBot.bean.qzoneSuosuo


import com.fasterxml.jackson.annotation.JsonProperty

data class PicinfoX(
    @JsonProperty("albumid")
    val albumid: String,
    @JsonProperty("height")
    val height: Int,
    @JsonProperty("lloc")
    val lloc: String,
    @JsonProperty("pre")
    val pre: String,
    @JsonProperty("sloc")
    val sloc: String,
    @JsonProperty("url")
    val url: String,
    @JsonProperty("width")
    val width: Int
)