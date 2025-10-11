package ru.debugger4o4.assistant.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Проверка работы сервиса
 */
@RestController
@RequestMapping("/healthCheck")
class HealthCheck {

    @GetMapping("/check")
    fun check(): String {
        return "Hello World!"
    }
}