package cn.awalol.qzoneBot.bean.musicCard


import com.fasterxml.jackson.annotation.JsonProperty

data class Config(
    @JsonProperty("autosize")
    val autosize: Boolean,
    @JsonProperty("ctime")
    val ctime: Int,
    @JsonProperty("forward")
    val forward: Boolean,
    @JsonProperty("token")
    val token: String,
    @JsonProperty("type")
    val type: String
)