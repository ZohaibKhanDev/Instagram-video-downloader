package com.example.instagram_video_downloader.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstagramDownloader(
    @SerialName("channelName")
    val channelName: String,
    @SerialName("channelThumbnail")
    val channelThumbnail: String,
    @SerialName("downloadUrl")
    val downloadUrl: String,
    @SerialName("videoThumbnail")
    val videoThumbnail: String,
    @SerialName("videoTitle")
    val videoTitle: String
)