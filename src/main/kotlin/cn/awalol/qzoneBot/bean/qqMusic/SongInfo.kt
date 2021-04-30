package cn.awalol.qzoneBot.bean.qqMusic


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SongInfo(
    @JsonProperty("code")
    val code: Int,
    @JsonProperty("data")
    val `data`: List<Data>,
    @JsonProperty("extra_data")
    val extraData: List<Any>,
    @JsonProperty("joox")
    val joox: Int,
    @JsonProperty("joox_login")
    val jooxLogin: Int,
    @JsonProperty("msgid")
    val msgid: Int,
    @JsonProperty("url1")
    val url1: Url1
)