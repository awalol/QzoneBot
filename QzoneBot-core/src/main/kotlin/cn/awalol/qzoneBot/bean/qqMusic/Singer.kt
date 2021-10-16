package cn.awalol.qzoneBot.bean.qqMusic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class Singer(
    @JsonProperty("mid")
    val mid: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("title")
    val title: String
)