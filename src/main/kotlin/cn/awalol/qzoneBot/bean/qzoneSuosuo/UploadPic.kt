package cn.awalol.qzoneBot.bean.qzoneSuosuo


import com.fasterxml.jackson.annotation.JsonProperty

internal data class UploadPic(
    @JsonProperty("filelen")
    val filelen: Int,
    @JsonProperty("filemd5")
    val filemd5: String
)