import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import sun.plugin.viewer.LifeCycleManager
import kotlin.coroutines.CoroutineContext

class MyActivity: LifeCycleManager(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job
}