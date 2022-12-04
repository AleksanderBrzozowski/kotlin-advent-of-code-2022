import java.lang.IllegalArgumentException

fun main() {
    fun Char.priority(): Int = if (this.isUpperCase()) this.code - 38 else this.code - 96

    fun String.splitIntoCompartments(): Pair<String, String> = take(length / 2) to substring(length / 2)

    fun Pair<String, String>.itemInBothCompartments(): Char {
        val (first, second) = this
        return first.find { second.contains(it) }
            ?: throw IllegalArgumentException("No item found that is present in both compartments")
    }

    fun List<String>.commonItem(): Char {
        val first = first()
        val rest = drop(1)
        return first.find { item ->
            rest.all { items ->
                items.contains(item)
            }
        } ?: throw IllegalArgumentException("No item found that is present in all compartments")
    }

    val testResult = readInput("Day03").sumOf {
        it.splitIntoCompartments()
            .itemInBothCompartments()
            .priority()
    }
    println(testResult)

    val result = readInput("Day03")
        .foldIndexed(emptyList<List<String>>()) { index, rucksacks, input ->
            if (index % 3 == 0) rucksacks + listOf(listOf(input)) else rucksacks.replaceLast { it + input }
        }
        .sumOf { it.commonItem().priority() }
    println(result)
}
