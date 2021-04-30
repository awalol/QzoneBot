package cn.awalol.qzoneBot.bean.musicCard


import com.fasterxml.jackson.annotation.JsonProperty

data class Meta(
    @JsonProperty("music")
    val music: Music?,
    @JsonProperty("news")
    val news: News?
)