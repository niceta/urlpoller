package training.poller.init

import io.micronaut.context.annotation.Context
import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton
import training.poller.service.PollingService

@Context
class PollingInitializer(
    private val pollingService: PollingService
) {
    @PostConstruct
    fun init() {
        pollingService.start()
        println("Polling service started")
    }
}