package org.example

import kotlinx.coroutines.*

suspend fun runMany(
    num: Int,
    iterations: Int,
    n: Int,
    farmer: Farmer,
    fox: Fox,
): Double =
    coroutineScope {
        val jobs =
            List(num) {
                async(Dispatchers.Default) { runSim(iterations, n, farmer, fox) }
            }
        jobs.sumOf { it.await() } / num
    }

suspend fun main() {
    val n = 15
    val result = runMany(1, 1, n, RandomFarmer(n), RandomFox())
    println(result)
}

fun runSim(
    iterations: Int,
    n: Int,
    farmer: Farmer,
    fox: Fox,
): Double {
    val random = kotlin.random.Random
    var totalDays = 0

    repeat(iterations) {
        var count = 0
        var foxPos = random.nextInt(1, n)

        for (i in 1..10000) {
            // Move fox
            if (foxPos == 1) {
                foxPos++
            } else if (foxPos == n) {
                foxPos--
            } else {
                if (fox.leftOrRight()) {
                    foxPos++
                } else {
                    foxPos--
                }
            }

            // Farmer selects
            val selection = farmer.next()

            // If correct end
            if (selection == foxPos) {
                totalDays += i
                break
            }
        }
    }

    return totalDays.toDouble() / iterations
}

abstract class Farmer(
    protected val n: Int,
) {
    abstract fun next(): Int
}

class RandomFarmer(
    n: Int,
) : Farmer(n) {
    private val random = kotlin.random.Random

    override fun next(): Int = random.nextInt(1, n)
}

class Optimal5Farmer : Farmer(5) {
    private var index = -1

    override fun next(): Int {
        index = (index + 1) % (n - 2)
        return index + 2
    }
}

abstract class Fox {
    // left == false, right == true
    abstract fun leftOrRight(): Boolean
}

class RandomFox : Fox() {
    private val random = kotlin.random.Random

    override fun leftOrRight(): Boolean = random.nextBoolean()
}
