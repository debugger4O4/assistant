package ru.debugger4o4.assistant.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.debugger4o4.assistant.dto.Promt
import ru.debugger4o4.assistant.dto.ResponseBodyData
import java.io.FileInputStream
import java.security.KeyStore
import java.security.SecureRandom


@Component
class KeystoreUtil {

    @Value("\${keystore.path}")
    private lateinit var keystorePath: String

    @Value("\${keystore.pass}")
    private lateinit var keystorePass: String

    private val logger: Logger = LoggerFactory.getLogger(KeystoreUtil::class.java)

    fun setCertificates() {
        try {
            val keyStore = loadKeyStore(keystorePath, keystorePass)
            val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            tmf.init(keyStore)
            val trustManagers = tmf.trustManagers
            val sslContext: SSLContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagers, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
        } catch (e: Exception) {
            logger.error("Util exception in setCertificates: {}", e.message)
        }
    }

    private fun loadKeyStore(keystorePath: String, keystorePass: String): KeyStore? {
        var ks: KeyStore? = null
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType())
            FileInputStream(keystorePath).use { fis -> ks.load(fis, keystorePass.toCharArray()) }
        } catch (e: Exception) {
            logger.error("Util exception in loadKeyStore: {}", e.message)
        }
        return ks
    }

    fun getAccessToken(responseBody: String?): String? {
        val mapper = ObjectMapper()
        try {
            val responseBodyData: ResponseBodyData = mapper.readValue(responseBody, ResponseBodyData::class.java)
            return responseBodyData.getAccessToken()
        } catch (e: JsonProcessingException) {
            logger.error("Util exception in getAccessToken: {}", e.message)
        }
        return null
    }

    fun getContent(responseBody: String?): String? {
        val mapper = ObjectMapper()
        try {
            val promt: Promt = mapper.readValue(responseBody, Promt::class.java)
            if (promt.getChoices() != null && promt.getChoices()!!.isNotEmpty()) {
                return promt.getChoices()!![0].getMessage()?.getContent()
            }
        } catch (e: JsonProcessingException) {
            logger.error("Util exception in getContent: {}", e.message)
        }
        return null
    }
}