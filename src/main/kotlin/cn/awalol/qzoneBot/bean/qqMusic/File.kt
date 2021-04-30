package cn.awalol.qzoneBot.bean.qqMusic


import com.fasterxml.jackson.annotation.JsonProperty

data class File(
    @JsonProperty("b_30s")
    val b30s: Int,
    @JsonProperty("e_30s")
    val e30s: Int,
    @JsonProperty("hires_bitdepth")
    val hiresBitdepth: Int,
    @JsonProperty("hires_sample")
    val hiresSample: Int,
    @JsonProperty("media_mid")
    val mediaMid: String,
    @JsonProperty("size_128mp3")
    val size128mp3: Int,
    @JsonProperty("size_192aac")
    val size192aac: Int,
    @JsonProperty("size_192ogg")
    val size192ogg: Int,
    @JsonProperty("size_24aac")
    val size24aac: Int,
    @JsonProperty("size_320mp3")
    val size320mp3: Int,
    @JsonProperty("size_48aac")
    val size48aac: Int,
    @JsonProperty("size_96aac")
    val size96aac: Int,
    @JsonProperty("size_96ogg")
    val size96ogg: Int,
    @JsonProperty("size_ape")
    val sizeApe: Int,
    @JsonProperty("size_dts")
    val sizeDts: Int,
    @JsonProperty("size_flac")
    val sizeFlac: Int,
    @JsonProperty("size_hires")
    val sizeHires: Int,
    @JsonProperty("size_try")
    val sizeTry: Int,
    @JsonProperty("try_begin")
    val tryBegin: Int,
    @JsonProperty("try_end")
    val tryEnd: Int,
    @JsonProperty("url")
    val url: String
)