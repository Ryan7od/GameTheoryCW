package org.example

import kotlinx.coroutines.*

suspend fun runMany(
    num: Int,
    iterations: Int,
    n: Int,
    farmerFactory: FarmerFactory,
    foxFactory: FoxFactory,
): Double =
    coroutineScope {
        val jobs =
            List(num) {
                async(Dispatchers.Default) { runSim(iterations, n, farmerFactory, foxFactory) }
            }
        jobs.sumOf { it.await() } / num
    }

suspend fun main() {
    val n = 5
    val result = runMany(10, 100000000, n, OptimalFarmerFactory(n), RandomFoxFactory())
    println(result)
}

fun runSim(
    iterations: Int,
    n: Int,
    farmerFactory: FarmerFactory,
    foxFactory: FoxFactory,
): Double {
    val random = kotlin.random.Random
    var totalDays = 0

    repeat(iterations) {
        var count = 0
        var foxPos = random.nextInt(1, n)
        val farmer = farmerFactory.buildFarmer()
        val fox = foxFactory.buildFox()

        var i = 1
        while (true) {
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
            i++
        }
    }

    return totalDays.toDouble() / iterations
}

abstract class FarmerFactory (
    protected val n: Int,
) {
    abstract fun buildFarmer(): Farmer
}

class RandomFarmerFactory (
    n: Int,
) : FarmerFactory(n) {
    override fun buildFarmer(): Farmer =
        RandomFarmer(n)
}

class OptimalFarmerFactory (
    n: Int,
): FarmerFactory(n) {
    override fun buildFarmer(): Farmer =
        OptimalFarmer(n)
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

    override fun next(): Int = random.nextInt(1, n+1)
}

class OptimalFarmer(n: Int) : Farmer(n) {
    private var index = 0
    private var strategy = (2..n-1) + (n-1 downTo 2)

    override fun next(): Int {
        return strategy[index++]
    }
}

abstract class FoxFactory() {
    abstract fun buildFox(): Fox
}

class RandomFoxFactory : FoxFactory() {
    override fun buildFox(): Fox =
        RandomFox()
}

abstract class Fox {
    // left == false, right == true
    abstract fun leftOrRight(): Boolean
}

class RandomFox : Fox() {
    private val random = kotlin.random.Random

    override fun leftOrRight(): Boolean = random.nextBoolean()
}
