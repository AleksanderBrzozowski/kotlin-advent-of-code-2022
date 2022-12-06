fun main() {
    data class Instruction(val quantity: Int, val from: Int, val to: Int)

    fun readCrates(): Pair<List<ArrayDeque<Char>>, List<Instruction>> {
        val input = readInput("Day05_test")
        return input.takeWhile { !it.contains("1") }
            .reversed()
            .foldIndexed(emptyList<ArrayDeque<Char>>()) { index, stacksOfCrates, line ->
                val crates = generateSequence(1) { it + 4 }
                    .takeWhile { it < line.length - 1 }
                    .map { line[it] }
                if (index == 0) {
                    crates.map { if (it == ' ') ArrayDeque() else ArrayDeque(listOf(it)) }
                        .toList()
                } else {
                    crates.forEachIndexed { crateIndex, character ->
                        if (character != ' ') {
                            stacksOfCrates[crateIndex].addFirst(character)
                        }
                    }
                    stacksOfCrates
                }
            } to input.reversed().takeWhile { it.isNotBlank() }
            .map { line ->
                val quantity = line.substringAfter("move ").takeWhile { it.isDigit() }.toInt()
                val from = line.substringAfter("from ").takeWhile { it.isDigit() }.toInt()
                val to = line.substringAfter("to ").takeWhile { it.isDigit() }.toInt()
                Instruction(quantity = quantity, from = from - 1, to = to - 1)
            }.reversed()
    }

    fun applyInstructionsFirstPart(stacksOfCrates: List<ArrayDeque<Char>>, instructions: List<Instruction>) {
        instructions.forEach { instruction ->
            repeat(instruction.quantity) {
                val removed = stacksOfCrates[instruction.from].removeFirst()
                stacksOfCrates[instruction.to].addFirst(removed)
            }
        }
    }

    fun applyInstructionsSecondPart(stacksOfCrates: List<ArrayDeque<Char>>, instructions: List<Instruction>) {
        instructions.forEach { instruction ->
            val removed = generateSequence { stacksOfCrates[instruction.from].removeFirst() }
                .take(instruction.quantity)
                .toList()
                .reversed()
            removed
                .forEach {
                    stacksOfCrates[instruction.to].addFirst(it)
                }
        }
    }

    val (testStacksOfCrates, testInstructions) = readCrates()
    applyInstructionsFirstPart(testStacksOfCrates, testInstructions)
    println(testStacksOfCrates.joinToString(separator = "") { it.first().toString() })

    val (stacksOfCrates, instructions) = readCrates()
    applyInstructionsSecondPart(stacksOfCrates, instructions)
    println(stacksOfCrates.joinToString(separator = "") { it.first().toString() })
}
