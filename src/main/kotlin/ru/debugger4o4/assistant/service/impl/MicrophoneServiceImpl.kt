package ru.debugger4o4.assistant.service.impl

import org.springframework.stereotype.Service
import ru.debugger4o4.assistant.service.MicrophoneService
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine

@Service
class MicrophoneServiceImpl: MicrophoneService {

    private var targetDataLine: TargetDataLine? = null

    override fun getMicrophones(): String {
        var response = ""
        val mixers = AudioSystem.getMixerInfo()
        mixers.forEachIndexed { index, mixer ->
            response += "${index + 1}. ${mixer.name}</br>"
        }
        return response
    }

    override fun startRecord() {
    }

    override fun stopRecord(byteArray: ByteArray): String {
        return "Stop Record"
    }

    fun printData(buffer: List<Byte>) {
        buffer.chunked(2).forEach { chunk ->
            val sample = chunk.joinToString("") { it.toInt().toString(16).padStart(2, '0') }
            print("$sample ")
        }
        println()
    }
}