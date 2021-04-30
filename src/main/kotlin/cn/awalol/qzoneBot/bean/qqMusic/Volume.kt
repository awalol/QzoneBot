package cn.awalol.qzoneBot.bean.qqMusic


import com.fasterxml.jackson.annotation.JsonProperty

data class Volume(
    @JsonProperty("gain")
    val gain: Double,
    @JsonProperty("lra")
    val lra: Double,
    @JsonProperty("peak")
    val peak: Int
)