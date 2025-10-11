package ru.debugger4o4.assistant.service.impl

import org.springframework.stereotype.Service
import ru.debugger4o4.assistant.service.MicrophoneService
import javax.sound.sampled.AudioSystem

@Service
class MicrophoneServiceImpl: MicrophoneService {

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
}