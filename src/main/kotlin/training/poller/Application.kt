package training.poller

import io.micronaut.runtime.Micronaut.run
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import training.poller.service.UrlPoller

fun main(args: Array<String>) {
	val urls = setOf("http://www.google.com/", "https://www.ya.ru/")
	val interval = 100L
	val poller = UrlPoller(urls, interval)
	poller.start()

	runBlocking {
		delay(60_000)
	}
	// run(*args)
}
