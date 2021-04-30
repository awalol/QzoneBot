package cn.awalol.qzoneBot.bean.musicCard


import com.fasterxml.jackson.annotation.JsonProperty

data class News(
    @JsonProperty("action")
    val action: String,
    @JsonProperty("android_pkg_name")
    val androidPkgName: String,
    @JsonProperty("app_type")
    val appType: Int,
    @JsonProperty("appid")
    val appid: Int,
    @JsonProperty("desc")
    val desc: String,
    @JsonProperty("jumpUrl")
    val jumpUrl: String,
    @JsonProperty("preview")
    val preview: String,
    @JsonProperty("source_icon")
    val sourceIcon: String,
    @JsonProperty("source_url")
    val sourceUrl: String,
    @JsonProperty("tag")
    val tag: String,
    @JsonProperty("title")
    val title: String
)