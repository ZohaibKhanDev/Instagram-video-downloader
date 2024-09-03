package com.example.instagram_video_downloader.data.repository

import com.example.instagram_video_downloader.domain.model.InstagramDownloader

interface ApiClient {
    suspend fun getInstagramVideos(url: String , igsh: String): InstagramDownloader?
}
