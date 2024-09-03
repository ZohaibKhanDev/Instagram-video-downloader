package com.example.instagram_video_downloader.domain.repository

import com.example.instagram_video_downloader.data.remote.DownloaderApiClient
import com.example.instagram_video_downloader.data.repository.ApiClient
import com.example.instagram_video_downloader.domain.model.InstagramDownloader

class Repository : ApiClient {
    override suspend fun getInstagramVideos(url: String, igsh: String): InstagramDownloader? {
     return  DownloaderApiClient.downloadInstagramVideo(url,igsh)
    }
}

