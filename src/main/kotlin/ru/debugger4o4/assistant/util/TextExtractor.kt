package ru.debugger4o4.assistant.util

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.nio.file.Files

//import com.yandex.cloud.sdk.auth.AuthProvider
//import com.yandex.cloud.sdk.auth.ServiceAccountKeyAuthProvider
//import com.yandex.cloud.sdk.auth.IamTokenService
import java.util.*

val token = generateToken()
const val audioFilePath = "record/recording.wav"

fun textExtract() {
    val audioFile = File(audioFilePath)
    val audioBytes = Files.readAllBytes(audioFile.toPath())

    val body = JSONObject()
        .put("config", JSONObject()
            .put("specification", JSONObject()
                .put("languageCode", "ru-RU")
                .put("profanityFilter", false))) // фильтрация ненормативной лексики
        .put("audio", JSONObject()
            .put("uri", ""))
        .put("audioContent", audioBytes.toBase64String())

    val jsonMediaType = "application/json; charset=utf-8".toMediaType()
    val requestBody = body.toString().toRequestBody(jsonMediaType)

    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://stt.api.cloud.yandex.net/speech/v1/stt:recognize")
        .addHeader("Authorization", "Bearer $token")
        .post(requestBody)
        .build()

    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val resultJson = JSONObject(response.body?.string())
        val text = resultJson.optString("result", "")
        println("Распознанный текст: $text")
    } else {
        println("Ошибка: ${response.message}")
    }
}

private fun ByteArray.toBase64String(): String = Base64.getEncoder().encodeToString(this)


fun generateToken() {
//    val keyFilePath = "service_account_key.json"
//
//    val provider = AuthProvider.create(ServiceAccountKeyAuthProvider(keyFilePath))
//    val iamTokenService = IamTokenService(provider)
//    val iamToken = iamTokenService.createToken()
//
//    println("Токен IAM: $iamToken")
}