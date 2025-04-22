package training.poller

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import training.poller.service.PollingService
import io.micronaut.runtime.Micronaut.run

fun main(args: Array<String>) {
//	val urls = setOf("http://www.google.com/", "https://www.ya.ru/")
//	val interval = 1000L
//	val poller = PollingService(urls, interval)
//	poller.start()
//
//	runBlocking {
//		delay(60_000)
//	}
	run(*args)
}
