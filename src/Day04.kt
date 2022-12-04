fun main() {
    fun IntRange.fullyOverlaps(other: IntRange) = this.first >= other.first && this.last <= other.last

    fun IntRange.partiallyOverlaps(other: IntRange) = this.any { other.contains(it) }

    fun String.toRange(): Pair<IntRange, IntRange> {
        val (firstRange, secondRange) = split(",")
            .map {
                val (start, end) = it.split("-")
                IntRange(start = start.toInt(), endInclusive = end.toInt())
            }
        return firstRange to secondRange
    }

    val testResult = readInput("Day04")
        .count {
            val (first, second) = it.toRange()
            first.fullyOverlaps(second) || second.fullyOverlaps(first)
        }
    println(testResult)

    val result = readInput("Day04")
        .count {
            val (first, second) = it.toRange()
            first.partiallyOverlaps(second) || second.partiallyOverlaps(first)
        }
    println(result)
}
