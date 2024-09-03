package com.example.instagram_video_downloader.di

import com.example.instagram_video_downloader.domain.repository.Repository
import com.example.instagram_video_downloader.presentation.viewmodel.MainViewModel
import org.koin.dsl.module

val appModule = module {
    single { Repository() }
    single { MainViewModel(get()) }
}