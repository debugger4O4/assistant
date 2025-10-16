package ru.debugger4o4.assistant.util

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets


@Component
class TextExtractor {

    @Value("\${token}")
    private lateinit var token: String

    @Value("\${catalog}")
    private lateinit var catalog: String

    @Value("\${audio.file.path}")
    private lateinit var audioFilePath: String

    fun textExtract() {

        val audioData = FileInputStream(audioFilePath).use { it.readAllBytes() }

        val params = listOf(
            "topic=general",
            "folderId=$catalog",
            "lang=ru-RU"
        ).joinToString("&")

        val url = URL("https://stt.api.cloud.yandex.net/speech/v1/stt:recognize?$params")

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", "application/octet-stream")
            setRequestProperty("Authorization", "Bearer $token")

            outputStream.write(audioData)

            if (responseCode in 200..299) {
                val gson = Gson()
                val result = gson.fromJson(
                    inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() },
                    Map::class.java
                )

                if (result["error_code"] == null) {
                    println(result["result"])
                } else {
                    println("Ошибка: ${result["message"]}")
                }
            } else {
                println("Ошибка запроса: Код состояния $responseCode")
            }
        }
    }
}
