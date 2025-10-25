package ru.debugger4o4.assistant.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.util.concurrent.atomic.AtomicReference


@Component
class TokenGenerator {

    @Value("\${gigachat.rq.uid}")
    private lateinit var rqUID: String

    @Value("\${gigachat.openapi.key}")
    private lateinit var openApiKey: String

    @Value("\${gigachat.get.token.url}")
    private lateinit var getTokenUrl: String

    private var util: KeystoreUtil? = null

    private val logger: Logger = LoggerFactory.getLogger(TokenGenerator::class.java)

    private val currentToken = AtomicReference("")

    @Scheduled(fixedRate = 29 * 60 * 1000)
    fun updateToken() {
        try {
            util?.setCertificates()
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
            headers.accept = listOf(MediaType.APPLICATION_JSON)
            headers.set("RqUID", rqUID)
            headers.setBasicAuth(openApiKey)
            val body: MultiValueMap<String, String> = LinkedMultiValueMap()
            body.add("scope", "GIGACHAT_API_PERS")
            val entity: HttpEntity<MultiValueMap<String, String>> = HttpEntity(body, headers)
            val restTemplate = RestTemplate()
            val response = restTemplate.exchange(
                getTokenUrl,
                HttpMethod.POST,
                entity,
                String::class.java
            )
            val newToken: String? = util?.getAccessToken(response.body)
            currentToken.set(newToken)
            logger.info("Token updated")
        } catch (e: Exception) {
            logger.error("Token updated error: {}", e.message)
        }
    }

    @Autowired
    fun setUtil(util: KeystoreUtil?) {
        this.util = util
    }


    fun getCurrentToken(): String? {
        return currentToken.get()
    }
}