package training.poller.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class UrlPoller {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
}