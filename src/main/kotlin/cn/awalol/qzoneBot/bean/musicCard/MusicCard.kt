package cn.awalol.qzoneBot.bean.musicCard


import com.fasterxml.jackson.annotation.JsonProperty

data class MusicCard(
    @JsonProperty("app")
    val app: String,
    @JsonProperty("config")
    val config: Config,
    @JsonProperty("desc")
    val desc: String,
    @JsonProperty("extra")
    val extra: Extra,
    @JsonProperty("meta")
    val meta: Meta,
    @JsonProperty("prompt")
    val prompt: String,
    @JsonProperty("ver")
    val ver: String,
    @JsonProperty("view")
    val view: String
)