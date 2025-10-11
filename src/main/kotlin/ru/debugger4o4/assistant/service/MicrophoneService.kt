package ru.debugger4o4.assistant.service

interface MicrophoneService {

    fun getMicrophones(): String

    fun startRecord()

    fun stopRecord(byteArray: ByteArray): String
}