package training.poller.controller

import io.micronaut.http.annotation.*
import jakarta.inject.Singleton
import training.poller.service.PollingService

@Singleton
@Controller("/polling")
class PollingController(
    private val pollingService: PollingService
) {

    @Put("/interval")
    fun updateInterval(@QueryValue intervalMillis: Long) {
        pollingService.updatePollingInterval(intervalMillis)
        println("interval has been updated")
    }

    @Post("/url")
    fun addUrl(@QueryValue url: String) {
        pollingService.addUrl(url)
        println("url $url has been added")
    }

    @Delete("/url")
    fun removeUrl(@QueryValue url: String) {
        pollingService.removeUrl(url)
        println("url $url has been removed")
    }

    @Get("/urls")
    fun getUrls() = pollingService.getUrls().also {
        println("urls: ${it.joinToString { " " }}")
    }
}