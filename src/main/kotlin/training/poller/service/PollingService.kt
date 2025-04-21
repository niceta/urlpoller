package training.poller.service

import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URI
import jakarta.inject.Singleton
import java.util.concurrent.atomic.AtomicLong

@Singleton
class PollingService(
    private val urls: Set<String>,
    pollingIntervalMillis: Long
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val pollingIntervalMillis = AtomicLong(pollingIntervalMillis)

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
        pollingIntervalMillis.set(newPollingInterval)
    }

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