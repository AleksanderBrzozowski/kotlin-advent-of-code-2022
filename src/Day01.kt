fun main() {
    val testInput = elves("Day01_test")
    println(testInput.maxBy { it.calories.sum() }.calories.sum())

    val input = elves("Day01")
    val (first, second, third) = input.map { it.calories.sum() }.sortedByDescending { it }
    println(first + second + third)
}

private data class Elf(val calories: List<Int>)

private fun elves(name: String): List<Elf> = readInput(name)
    .fold(listOf(Elf(calories = emptyList()))) { elves, input ->
        if (input == "") {
            return@fold elves + Elf(calories = emptyList())
        }
        val currentElf = elves.last()
        elves.replaceLast(currentElf.copy(calories = currentElf.calories + input.toInt()))
    }
