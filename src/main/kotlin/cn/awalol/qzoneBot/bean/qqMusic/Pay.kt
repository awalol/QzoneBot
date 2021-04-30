package cn.awalol.qzoneBot.bean.qqMusic


import com.fasterxml.jackson.annotation.JsonProperty

data class Pay(
    @JsonProperty("pay_down")
    val payDown: Int,
    @JsonProperty("pay_month")
    val payMonth: Int,
    @JsonProperty("pay_play")
    val payPlay: Int,
    @JsonProperty("pay_status")
    val payStatus: Int,
    @JsonProperty("price_album")
    val priceAlbum: Int,
    @JsonProperty("price_track")
    val priceTrack: Int,
    @JsonProperty("time_free")
    val timeFree: Int
)