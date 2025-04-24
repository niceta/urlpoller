package training.poller.service

import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URI
import jakarta.inject.Singleton
import training.poller.config.PollingServiceConfig
import java.net.MalformedURLException
import java.util.Collections
import java.util.concurrent.atomic.AtomicLong

sealed class PollingException(message: String): RuntimeException(message)

class TooShortIntervalException(min: Long): PollingException("Interval must be >= $min ms")
class InvalidUrlException(url: String): PollingException("Invalid URL: $url")
class UrlNotFoundException(url: String): PollingException("URL not found: $url")


@Singleton
class PollingService(
    config: PollingServiceConfig
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val pollingIntervalMillis = AtomicLong(config.intervalMs)
    private val urls = Collections.synchronizedSet(config.initialUrls)

    fun start() {
        scope.launch {
            while (true) {
                coroutineScope {
                    urls.forEach { url ->
                        launch {
                            checkUrl(url)
                        }
                    }
                }
                println("делэюсь")
                delay(pollingIntervalMillis.get())
            }
        }
    }

    fun updatePollingInterval(newPollingInterval: Long) {
        val minIntervalMs = 1000L
        if (newPollingInterval < minIntervalMs) {
            throw TooShortIntervalException(minIntervalMs)
        }
        pollingIntervalMillis.set(newPollingInterval)
    }
    fun addUrl(url: String) {
        try {
            URI(url).toURL()
        } catch(error: MalformedURLException) {
            throw InvalidUrlException(url)
        }
        urls.add(url)
    }

    fun removeUrl(url: String) {
        if (!urls.remove(url)) {
            throw UrlNotFoundException(url)
        }
    }
    fun getUrls() = urls.toList()

    private suspend fun checkUrl(url: String) {
        withContext(Dispatchers.IO) {
            println("чекаю урл $url")
            var connection: HttpURLConnection? = null
            val timestamp = java.time.LocalDateTime.now().toString()
            try {
                connection = (URI(url).toURL().openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    val timeout = 5000
                    connectTimeout = timeout
                    readTimeout = timeout
                }
                val responseCode = connection.responseCode
                println("[$timestamp] url: $url - status: $responseCode")
            } catch (e: Exception) {
                println("Error checking $url: ${e.message}")
            } finally {
                connection?.disconnect()
            }
        }
    }
}