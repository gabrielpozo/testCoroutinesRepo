import kotlinx.coroutines.*
import java.io.IOException
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis


fun main(args: Array<String>) {


    //exampleLaunchCoroutineScope()
    //exampleThreads()

    runningConcurrently()
    runBlocking {
        /*   methodToPass("", 0)
           methodToPass("", 2)*/
    }

    //exampleCoroutineScope()

    println("end of main")
}

fun exampleCoroutineScope() = runBlocking {
    launch {
        var job = launch {

            delay(2000)
            println("end launch 1")
        }

        var job2 = launch {

            delay(2000)
            println("end launch 2")
        }

        println("end main launch")
    }
    println("end of runBlocking coroutine ")
}


fun exampleLaunchCoroutineScope() = runBlocking {

    println("one from ${Thread.currentThread().name}")

    var job = async {
        var startTime = System.currentTimeMillis()
        println("thread of beginning launch builder: ${Thread.currentThread().name}")

        val deferred1 = async { printlnDelayedReturn("KO from deferred 1 ${Thread.currentThread().name}") }.await()
        val deferred2 =
            async { printlnDelayedReturn("KO from deferred 2 ${Thread.currentThread().name}") }.await()
        val deferred3 =
            async(Dispatchers.IO) { printlnDelayedReturn("KO from deferred 3 ${Thread.currentThread().name}") }.await()

        updateUI(deferred1 + deferred2 + deferred3)

        var endTime = System.currentTimeMillis()

        println("taken Time: ${startTime - endTime}")
    }



    println("both token recieve ")
    println(" three from ${Thread.currentThread().name}")

}

fun runningConcurrently() = runBlocking {

    val time = measureTimeMillis {

        val v1 = async { retry { f1(it) } }
        val v2 = async { retry { f2(it) } }

        println("Result = ${v1.await() + v2.await()}")
    }

    println("Completed in $time ms")

}

suspend fun <T> retry(condition: suspend (Int) -> T): T {
    for (i in 1..5) { // try 5 times
        // println("How may times $i")
        try {
            return withTimeout(500) {
                println("Current Thread: ${Thread.currentThread().name}")
                // with timeout
                condition(i)
            }
        } catch (e: TimeoutCancellationException) { /* retry */
            println("TimeOutCancellation $i")
        }
    }

    return condition(0) //last time just invoke without timeout
}

suspend fun <T> retryIO(block: suspend () -> T): T {
    var curDelay = 1000L
    while (true) {
        try {
            block()
        } catch (e: IOException) { /* retry */

        }
        delay(curDelay)
        curDelay = (curDelay * 2).coerceAtMost(60000L)
    }

}

suspend fun f1(i: Int): Int {
    println("f1 attempt $i")
    delay(if (i != 3) 2000 else 200)
    return 1
}

suspend fun f2(i: Int): Int {
    println("f2 attempt $i")
    delay(if (i != 3) 2000 else 200)
    return 2
}

fun updateUI(message: String) {
    println("update UI THREAD: ${Thread.currentThread().name} ")
    //updateRecyclerView()
}

fun exampleThreads() {
    print("Hallo")
    val jobs = List(100) {
        thread {
            Thread.sleep(1000L)
            print("thread")
        }
    }
    jobs.forEach { it.join() }
}

suspend fun printlnDelayedReturn(message: String): String {
    delay(3000)
    println(message)

    return message
}


/**
 * Practical Examples
 */

suspend fun methodToPass(message: String, calc: Int) = coroutineScope {
    println("method to pass 1 ")
    async {
        delay(2000)
        println("is done on delay")
    }

    println("method to pass 2 ")
}

suspend fun methodToPass2(message: String, calc: Int) = coroutineScope {
    println("method to pass 1 ")
    async {
        delay(4000)
        println("is done on delay")
    }

    println("method to pass 2 ")
}

fun <T> ArrayList<T>.funthatAcceptsAnotherFun(bloque_funcion: (T) -> Int) {
    for (item in this) {
        var valor = bloque_funcion(item)
        println("value of $valor")
    }
}

suspend fun <T> retryCopy(bloque_funcion: suspend (Int) -> T): T {
    for (i in 1..5) { // try 5 times
        // println("How may times $i")
        try {
            return withTimeout(500) {
                // with timeout
                bloque_funcion(i)
            }
        } catch (e: TimeoutCancellationException) { /* retry */
            println("TimeOutCancellation $i")
        }
    }
    return bloque_funcion(0) // last time just invoke without timeout
}

