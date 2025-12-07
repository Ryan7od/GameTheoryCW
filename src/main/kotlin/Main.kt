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

fun main() = runBlocking {
    for (n in 5..15) {
        val result = runMany(
            10,
            10000000,
            n,
            OptimalFarmerFactory(n),
            RandomFoxFactory(n)
        )
        println("$n: $result")
    }
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
        var foxPos = -1
        val farmer = farmerFactory.buildFarmer()
        val fox = foxFactory.buildFox()

        var i = 1
        while (true) {
            // Move fox
            foxPos = fox.next(foxPos)

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

abstract class FarmerFactory(
    protected val n: Int,
) {
    abstract fun buildFarmer(): Farmer
}

class RandomFarmerFactory(
    n: Int,
) : FarmerFactory(n) {
    override fun buildFarmer(): Farmer = RandomFarmer(n)
}

class OptimalFarmerFactory(
    n: Int,
) : FarmerFactory(n) {
    override fun buildFarmer(): Farmer = OptimalFarmer(n)
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

    override fun next(): Int = random.nextInt(1, n + 1)
}

class OptimalFarmer(
    n: Int,
) : Farmer(n) {
    private var index = 0
    private var strategy = (2..n - 1) + (n - 1 downTo 2)

    override fun next(): Int {
        val takeIndex = index
        index = (index + 1) % (n * 2 - 4)
        return strategy[takeIndex]
    }
}

abstract class FoxFactory(
    protected val n: Int,
) {
    abstract fun buildFox(): Fox
}

class RandomFoxFactory(
    n: Int,
) : FoxFactory(n) {
    override fun buildFox(): Fox = RandomFox(n)
}

class RandomWithStayFoxFactory(
    n: Int,
) : FoxFactory(n) {
    override fun buildFox(): Fox = RandomWithStayFox(n)
}

abstract class Fox(
    protected val n: Int,
) {
    abstract fun next(pos: Int): Int
}

class RandomFox(
    n: Int,
) : Fox(n) {
    private val random = kotlin.random.Random

    override fun next(pos: Int): Int =
        // Initialisation
        if (pos == -1) {
            random.nextInt(1, n+1)
        } else if (pos == 1) {
            pos + 1
        } else if (pos == n) {
            pos - 1
        } else {
            if (random.nextBoolean()) {
                pos + 1
            } else {
                pos - 1
            }
        }
}

class RandomWithStayFox(
    n: Int,
) : Fox(n) {
    private val random = kotlin.random.Random

    override fun next(pos: Int): Int =
        // Initialisation
        if (pos == -1) {
            random.nextInt(1, n+1)
        } else if (pos == 1) {
            if (random.nextBoolean()) {
                pos
            } else {
                pos + 1
            }
        } else if (pos == n) {
            if (random.nextBoolean()) {
                pos
            } else {
                pos - 1
            }
        } else {
            pos + random.nextInt(-1, 2)
        }
}
