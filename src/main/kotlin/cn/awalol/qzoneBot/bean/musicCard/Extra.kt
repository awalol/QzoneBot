package cn.awalol.qzoneBot.bean.musicCard


import com.fasterxml.jackson.annotation.JsonProperty

data class Extra(
    @JsonProperty("app_type")
    val appType: Int,
    @JsonProperty("appid")
    val appid: Int,
    @JsonProperty("msg_seq")
    val msgSeq: Long,
    @JsonProperty("uin")
    val uin: Long
)