package ru.debugger4o4.assistant.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties



@JsonIgnoreProperties(ignoreUnknown = true)
class ResponseBodyData {
    private var access_token: String? = null

    fun getAccessToken(): String? {
        return access_token
    }
}