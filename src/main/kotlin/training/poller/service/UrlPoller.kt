package training.poller.service

import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URI

class UrlPoller(
    private val urls: Set<String>,
    private val pollingIntervalMillis: Long
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

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
                delay(pollingIntervalMillis)
            }
        }
    }
    private suspend fun checkUrl(url: String) {
        withContext(Dispatchers.IO) {
            println("чекаю урл $url")
            var connection: HttpURLConnection? = null
            try {
                connection = (URI(url).toURL().openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    val timeout = 5000
                    connectTimeout = timeout
                    readTimeout = timeout
                }
                val responseCode = connection.responseCode
                println("Checked $url - Response code: $responseCode")
            } catch (e: Exception) {
                println("Error checking $url: ${e.message}")
            } finally {
                connection?.disconnect()
            }
        }
    }
}