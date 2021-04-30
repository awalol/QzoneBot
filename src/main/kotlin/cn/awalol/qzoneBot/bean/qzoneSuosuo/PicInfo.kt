package cn.awalol.qzoneBot.bean.qzoneSuosuo


import com.fasterxml.jackson.annotation.JsonProperty

data class PicInfo(
    @JsonProperty("picinfo")
    val picinfo: PicinfoX
)