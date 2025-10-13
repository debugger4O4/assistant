package ru.debugger4o4.assistant.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.debugger4o4.assistant.service.MicrophoneService


@RestController
@RequestMapping("/micro")
class MicrophoneController(private val microphoneService: MicrophoneService) {

    @GetMapping("/getMicrophones")
    fun getMicrophones(): String {
        return microphoneService.getMicrophones()
    }

    @GetMapping("/startRecord")
    fun startRecord() {
        microphoneService.startRecord()
    }

    @GetMapping("/stopRecord")
    fun stopRecord() {
        microphoneService.stopRecord()
    }
}