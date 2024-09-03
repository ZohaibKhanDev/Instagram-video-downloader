package com.example.instagram_video_downloader.presentation.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.instagram_video_downloader.domain.model.InstagramDownloader
import com.example.instagram_video_downloader.domain.usecase.ResultState
import com.example.instagram_video_downloader.presentation.viewmodel.MainViewModel
import org.koin.compose.koinInject

@Composable
fun HomeScreen() {
    val viewModel: MainViewModel = koinInject()
    var downloaderData by remember {
        mutableStateOf<InstagramDownloader?>(null)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }
    var url by remember {
        mutableStateOf("")
    }
    val state by viewModel.videoDownloadInfo.collectAsState()

    when (state) {
        is ResultState.Error -> {
            isLoading = false
            val error = (state as ResultState.Error).error
            SelectionContainer {
                Text(text = error.toString())
            }
        }

        ResultState.Loading -> {
            isLoading = true
        }

        is ResultState.Success -> {
            isLoading = false
            val success = (state as ResultState.Success).success
            downloaderData = success
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = url, onValueChange = {
            url = it
        }, trailingIcon = {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "",
                modifier = Modifier.clickable { viewModel.downloadInstagramVideo(url,"MzRlODBiNWFlZA==") })
        })

        if (isLoading){
            CircularProgressIndicator()
        }else{
            downloaderData?.let {
                AsyncImage(
                    model = it.videoThumbnail,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }

    }

}
