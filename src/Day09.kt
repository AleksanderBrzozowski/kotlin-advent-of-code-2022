import Day09.Direction.D
import Day09.Direction.L
import Day09.Direction.R
import Day09.Direction.U
import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.math.sign

class Day09 {
    enum class Direction { L, R, U, D }
}

fun main() {
    data class Point(val x: Int, val y: Int) {
        operator fun minus(point: Point): Point = Point(x - point.x, y - point.y)
        operator fun plus(point: Point): Point = Point(x + point.x, y + point.y)
    }

    fun Point.update(direction: Day09.Direction): Point = when (direction) {
        L -> copy(x = x - 1)
        R -> copy(x = x + 1)
        U -> copy(y = y + 1)
        D -> copy(y = y - 1)
    }

    fun Point.isTouching(other: Point): Boolean = abs(x - other.x) <= 1 && abs(y - other.y) <= 1

    val testData = readInput("Day09_test")
        .flatMap { line ->
            val (direction, times) = line.split(" ")
            (1..times.toInt()).map {
                when (direction) {
                    "L" -> L
                    "R" -> R
                    "U" -> U
                    "D" -> D
                    else -> throw IllegalArgumentException("Unknown direction: $direction")
                }
            }
        }

    fun part1() {
        var tail = Point(0, 0)
        var head = Point(0, 0)
        var visited = setOf<Point>(tail)
        testData.forEach { direction ->
            val prevHead = head
            head = head.update(direction)
            if (tail.isTouching(head)) {
                return@forEach
            }
            tail = prevHead
            visited = visited + tail
        }
        println(visited.size)
    }

    fun visualize(knot: List<Point>) {
        (-5..15).map { y ->
            // (0..4).map { y ->
            (-11..15).map { x ->
                // (0..5).map { x ->
                when (val indexOfPoint = knot.indexOf(Point(x, y))) {
                    -1 -> "."
                    0 -> "H"
                    else -> indexOfPoint.toString()
                }
            }
        }.reversed().onEach { println(it.joinToString(separator = " ")) }
    }

    fun visualizeVisited(visited: Set<Point>) {
        (-5..15).map { y ->
            (-11..15).map { x ->
                if (visited.contains(Point(x, y))) "#" else "."
            }
        }.reversed().onEach { println(it.joinToString(separator = " ")) }
    }

    fun part2() {
        fun Int.isHead(): Boolean = this == 0

        val knot = generateSequence { Point(0, 0) }.take(10).toMutableList()
        var visited = setOf(Point(0, 0))
        var last = testData[0]
        testData.forEach { direction ->
            repeat(knot.size) { index ->
                val point = knot[index]
                when {
                    index.isHead() -> {
                        knot[index] = point.update(direction)
                    }

                    else -> {
                        val precedingPoint = knot[index - 1]
                        if (point.isTouching(precedingPoint)) {
                            return@repeat
                        }
                        val movement = Point((precedingPoint.x - point.x).sign, (precedingPoint.y - point.y).sign)
                        knot[index] = point + movement
                    }
                }
                if (index == knot.size - 1) {
                    visited = visited + knot[index]
                }
            }
            if (last != direction) {
                last = direction
            }
            // visualize(knot)
            // println()
        }
        // visualizeVisited(visited)
        println(visited.size)
    }

    part1()
    part2()
}
