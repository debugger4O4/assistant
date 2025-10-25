package ru.debugger4o4.assistant.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
class RequestSender {

    @Value("\${gigachat.send.query.url}")
    private lateinit var sendQueryUrl: String

    private var util: KeystoreUtil? = null

    private var tokenGenerator: TokenGenerator? = null

    @Autowired
    fun setUtil(util: KeystoreUtil?) {
        this.util = util
    }

    @Autowired
    fun setTokenUpdater(tokenGenerator: TokenGenerator?) {
        this.tokenGenerator = tokenGenerator
    }

    fun sendQuery(question: String): String? {
        util!!.setCertificates()
        val payload = """
                {
                  "model": "%s",
                  "messages": [
                    {
                      "role": "user",
                      "content": "Ответь на вопрос: %s"
                    }
                  ],
                  "n": 1,
                  "stream": false,
                  "max_tokens": 512,
                  "repetition_penalty": 1,
                  "update_interval": 0
                }
                
                """.trimIndent().formatted("GigaChat-2-Max", question)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        tokenGenerator!!.getCurrentToken()?.let { headers.setBearerAuth(it) }
        val entity: HttpEntity<*> = HttpEntity(payload, headers)
        val restTemplate = RestTemplate()
        val response = restTemplate.exchange(sendQueryUrl, HttpMethod.POST, entity, String::class.java)
        return util!!.getContent(response.body)
    }
}