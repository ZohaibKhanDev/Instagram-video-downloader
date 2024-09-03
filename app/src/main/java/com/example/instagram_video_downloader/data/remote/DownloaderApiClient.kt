package com.example.instagram_video_downloader.data.remote

import com.example.instagram_video_downloader.domain.model.InstagramDownloader
import com.example.instagram_video_downloader.utils.Constant.TIMEOUT
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readText
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.io.IOException

object DownloaderApiClient {
    @OptIn(ExperimentalSerializationApi::class)
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
        }

        install(HttpTimeout) {
            connectTimeoutMillis = TIMEOUT
            socketTimeoutMillis = TIMEOUT
            requestTimeoutMillis = TIMEOUT
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun downloadInstagramVideo(baseUrl: String, igsh: String): InstagramDownloader? {
        val fullUrl = "$baseUrl&igsh=$igsh"

        return try {
            val response: HttpResponse = client.post("https://Video-Downloader.proxy-production.allthingsdev.co/instagram/download?url=$fullUrl") {
                headers {
                    append("x-apihub-key", "iD8GmEzCAPZM6byGbiw7hJ70nrbMXKknIAnzAwVc7a3UWlwL-8")
                    append("x-apihub-host", "Video-Downloader.allthingsdev.co")
                    append("x-apihub-endpoint", "6bd5f6f8-78ac-4ab5-8c1c-f41614768299")
                }
                contentType(ContentType.Application.Json)
            }

            val responseBody = response.bodyAsText()
            println("Response Body: $responseBody")

            if (response.status.isSuccess()) {
                Json.decodeFromString<InstagramDownloader>(responseBody)
            } else {
                println("Error response: ${response.status}")
                println("Error body: $responseBody")
                null
            }
        } catch (e: ClientRequestException) {
            println("Client error: ${e.message}")
            null
        } catch (e: ServerResponseException) {
            println("Server error: ${e.message}")
            null
        } catch (e: IOException) {
            println("Network error: ${e.message}")
            null
        }
    }

}