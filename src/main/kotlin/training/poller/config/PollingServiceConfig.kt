package training.poller.config

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("polling")
class PollingServiceConfig {
    var initialUrls = emptySet<String>()
    var intervalMs = 5000L
}