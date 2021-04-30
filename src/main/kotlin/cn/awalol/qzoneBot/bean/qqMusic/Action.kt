package cn.awalol.qzoneBot.bean.qqMusic


import com.fasterxml.jackson.annotation.JsonProperty

data class Action(
    @JsonProperty("alert")
    val alert: Int,
    @JsonProperty("icons")
    val icons: Int,
    @JsonProperty("msgdown")
    val msgdown: Int,
    @JsonProperty("msgfav")
    val msgfav: Int,
    @JsonProperty("msgid")
    val msgid: Int,
    @JsonProperty("msgpay")
    val msgpay: Int,
    @JsonProperty("msgshare")
    val msgshare: Int,
    @JsonProperty("switch")
    val switch: Int
)