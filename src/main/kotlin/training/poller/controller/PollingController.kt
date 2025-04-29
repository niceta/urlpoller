package training.poller.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import jakarta.inject.Singleton
import training.poller.service.PollingException
import training.poller.service.PollingService

@Singleton
@Controller("/polling")
class PollingController(
    private val pollingService: PollingService
) {

    @Put("/interval")
    fun updateInterval(@QueryValue intervalMillis: Long): HttpResponse<Any> = try {
        pollingService.updatePollingInterval(intervalMillis)
        HttpResponse.ok(mapOf("message" to "interval updated"))
    } catch (error: PollingException) {
        HttpResponse.badRequest(mapOf("error" to error.message))
    }


    @Post("/url")
    fun addUrl(@QueryValue url: String): HttpResponse<Any> = try {
        pollingService.addUrl(url)
        HttpResponse.ok(mapOf("message" to "URL added"))
    } catch (error: PollingException) {
        HttpResponse.badRequest(mapOf("error" to error.message))
    }

    @Delete("/url")
    fun removeUrl(@QueryValue url: String): HttpResponse<Any> = try {
        pollingService.removeUrl(url)
        HttpResponse.ok(mapOf("message" to "URL removed"))
    } catch (error: PollingException) {
        HttpResponse.badRequest(mapOf("error" to error.message))
    }

    @Get("/urls")
    fun getUrls() = pollingService.getUrls().also {
        println("urls: ${it.joinToString { " " }}")
    }
}