fun main() {
    val testInput = readInput("Day06_test")[0]
    val testResult = testInput
        .windowed(size = 4, step = 1)
        .indexOfFirst {
            it.toCharArray().distinct() == it.toCharArray().toList()
        }
    println(testResult + 4)

    val input = readInput("Day06_test")[0]
    val result = input
        .windowed(size = 14, step = 1)
        .indexOfFirst {
            it.toCharArray().distinct() == it.toCharArray().toList()
        }
    println(result + 14)
}
