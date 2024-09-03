package com.example.instagram_video_downloader.presentation.ui.screens.home

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.instagram_video_downloader.R
import com.example.instagram_video_downloader.domain.model.InstagramDownloader
import com.example.instagram_video_downloader.domain.usecase.ResultState
import com.example.instagram_video_downloader.presentation.viewmodel.MainViewModel
import org.koin.compose.koinInject
import java.io.OutputStream

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    val viewModel: MainViewModel = koinInject()
    var downloaderData by remember { mutableStateOf<InstagramDownloader?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var radioButton1 by remember { mutableStateOf(false) }
    var url by remember { mutableStateOf("") }
    val state by viewModel.videoDownloadInfo.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    when (state) {
        is ResultState.Error -> {
            isLoading = false
            val error = (state as ResultState.Error).error
            SelectionContainer {
                Text(text = error.toString())
            }
        }

        ResultState.Loading -> {
            if (isLoading) {
                isLoading = true
            }
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
                modifier = Modifier.size(47.dp),
                colorFilter = ColorFilter.tint(color = Color(0XFFfe0164))
            )
        }, actions = {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .width(39.dp)
                    .height(39.dp)
                    .border(
                        BorderStroke(1.dp, color = Color.LightGray),
                        shape = RoundedCornerShape(1.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "")
            }
        })
    }, floatingActionButton = {
        if (downloaderData?.videoThumbnail == null) {
        } else {
            FloatingActionButton(
                onClick = {
                    showBottomSheet = true
                },
                containerColor = Color(0XFFfe0164),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Outlined.Download,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(if (downloaderData?.videoThumbnail == null) 400.dp else 500.dp)
                        .border(
                            BorderStroke(1.dp, color = Color.LightGray),
                            shape = RoundedCornerShape(5.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Instagram Downloader",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = url,
                                onValueChange = { url = it },
                                modifier = Modifier.width(280.dp),
                                placeholder = {
                                    Text(text = "Paste Instagram link", color = Color.LightGray)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = ""
                                    )
                                },
                                textStyle = TextStyle(fontSize = 15.sp, color = Color.LightGray),
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                ),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Box(
                                modifier = Modifier
                                    .width(73.dp)
                                    .clickable {
                                        isLoading=true
                                        viewModel.downloadInstagramVideo(url, "MzRlODBiNWFlZA==")
                                    }
                                    .height(53.dp)
                                    .background(Color(0XFFfe0164)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "Start", color = Color.White)
                                    Spacer(modifier = Modifier.width(0.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "",
                                        modifier = Modifier.rotate(180f),
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "By using our service you are accepting our terms of service",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        if (isLoading) {
                            CircularProgressIndicator(color = Color(0XFFfe0164))
                        }

                        downloaderData?.let {
                            AsyncImage(
                                model = it.videoThumbnail,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 14.dp, end = 14.dp)
                                    .height(220.dp).clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Best Instagram Video Downloader",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Y2Mate is the fastest Instagram Downloader tool that allows you to easily convert and download videos and audios from youtube for free and in the best available quality. Y2Mate is the ultimate tool to download unlimited Instagram videos without any need for registration. You can quickly convert and download hundreds of videos and music files directly from Instagram.",
                    color = Color.DarkGray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                )
            }
        }
        var selectedQuality by remember { mutableStateOf("144p") }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
            ) {
                Text(
                    text = "Music",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 35.dp, start = 17.dp)
                )
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 17.dp, top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    VideoQualityOption(
                        quality = "128k",
                        isSelected = selectedQuality == "128k",
                        onSelect = { selectedQuality = "128k" }
                    )
                }

                Text(
                    text = "Video",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 35.dp, start = 17.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(9.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item {
                        VideoQualityOption(
                            quality = "144p",
                            isSelected = selectedQuality == "144p",
                            onSelect = { selectedQuality = "144p" }
                        )
                    }
                    item {
                        VideoQualityOption(
                            quality = "240p",
                            isSelected = selectedQuality == "240p",
                            onSelect = { selectedQuality = "240p" }
                        )
                    }
                    item {
                        VideoQualityOption(
                            quality = "360p",
                            isSelected = selectedQuality == "360p",
                            onSelect = { selectedQuality = "360p" }
                        )
                    }
                    item {
                        VideoQualityOption(
                            quality = "480p",
                            isSelected = selectedQuality == "480p",
                            onSelect = { selectedQuality = "480p" }
                        )
                    }
                    item {
                        VideoQualityOption(
                            quality = "720p HD",
                            isSelected = selectedQuality == "720p HD",
                            onSelect = { selectedQuality = "720p HD" }
                        )
                    }
                    item {
                        VideoQualityOption(
                            quality = "1080p HD",
                            isSelected = selectedQuality == "1080p HD",
                            onSelect = { selectedQuality = "1080p HD" }
                        )
                    }
                }


                Button(
                    onClick = {
                        showBottomSheet = false
                        downloaderData?.let {
                            val downloadUrl = it.downloadUrl
                            val fileName = it.videoTitle
                            val mimeType =
                                if (selectedQuality == "128k") "audio/mp3" else "video/mp4"

                            if (!downloadUrl.isNullOrEmpty()) {
                                downloadFile(context, downloadUrl, fileName, mimeType)
                            } else {
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0XFFfe0164),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Downloading,
                            contentDescription = "Download"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Download", fontWeight = FontWeight.Bold)
                    }
                }


            }
        }
    }
}

@Composable
fun VideoQualityOption(quality: String, isSelected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onSelect() },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelect() },
            colors = RadioButtonDefaults.colors(selectedColor = Color.Red)
        )
        Text(
            text = quality,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun downloadFile(context: Context, url: String, title: String?, mimeType: String) {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    val uri = Uri.parse(url)
    val request = DownloadManager.Request(uri)
        .setMimeType(mimeType)
        .setTitle(title)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$title.$mimeType")

    downloadManager.enqueue(request)
}

