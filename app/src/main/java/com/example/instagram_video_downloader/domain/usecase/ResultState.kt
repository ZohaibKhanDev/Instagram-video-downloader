package com.example.instagram_video_downloader.domain.usecase

sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()

    data class Success<T>(val success: T) : ResultState<T>()

    data class Error(val error: Throwable) : ResultState<Nothing>()
}