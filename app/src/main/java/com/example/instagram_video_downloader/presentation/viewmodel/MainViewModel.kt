package com.example.instagram_video_downloader.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagram_video_downloader.domain.model.InstagramDownloader
import com.example.instagram_video_downloader.domain.repository.Repository
import com.example.instagram_video_downloader.domain.usecase.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _videoDownloadInfo = MutableStateFlow<ResultState<InstagramDownloader?>>(ResultState.Loading)
    val videoDownloadInfo: StateFlow<ResultState<InstagramDownloader?>> = _videoDownloadInfo.asStateFlow()

    fun downloadInstagramVideo(url: String , igsh: String) {
        viewModelScope.launch {
            _videoDownloadInfo.value = ResultState.Loading
            try {
                val response = repository.getInstagramVideos(url,igsh)
                _videoDownloadInfo.value = ResultState.Success(response)
            } catch (e: Exception) {
                _videoDownloadInfo.value = ResultState.Error(e)
            }
        }
    }
}
