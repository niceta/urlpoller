package training.poller.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
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
}