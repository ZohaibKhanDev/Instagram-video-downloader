package com.example.instagram_video_downloader.presentation.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagram_video_downloader.R
import com.example.instagram_video_downloader.domain.model.InstagramDownloader
import com.example.instagram_video_downloader.domain.usecase.ResultState
import com.example.instagram_video_downloader.presentation.viewmodel.MainViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Y2Mate",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 33.sp,
                color = Color(0XFFfe0164)
            )
        }, navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.download),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(47
                    .dp),
                colorFilter = ColorFilter.tint(color = Color(0XFFfe0164))
            )
        }, actions = {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .width(39.dp)
                    .height(39.dp)
                    .border(
                        BorderStroke(1.dp, color = Color.LightGray.copy(alpha = 0.40f)),
                        shape = RoundedCornerShape(1.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "")
            }
        })
    }) {

    }

}
