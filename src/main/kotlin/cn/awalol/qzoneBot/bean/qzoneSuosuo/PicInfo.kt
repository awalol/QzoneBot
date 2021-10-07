package cn.awalol.qzoneBot.bean.qzoneSuosuo


import com.fasterxml.jackson.annotation.JsonProperty

internal data class PicInfo(
    @JsonProperty("picinfo")
    val picinfo: PicinfoX
)