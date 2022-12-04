import java.lang.IllegalArgumentException

class Part1 {
    fun run() {
        val testGame = game("Day02_test")
        println(testGame.sumOf { it.score() })
    }

    private fun String.toShape() = when (this) {
        "A", "X" -> Shape.ROCK
        "B", "Y" -> Shape.PAPER
        "C", "Z" -> Shape.SCISSORS
        else -> throw IllegalArgumentException("Unknown shape: $this")
    }

    private fun game(name: String): List<Round> {
        return readInput(name)
            .map {
                val (opponent, player) = it.split(" ")
                Round(player = player.toShape(), opponent = opponent.toShape())
            }
    }

    private data class Round(val opponent: Shape, val player: Shape) {
        fun score(): Int = player.score + result().score

        private fun result(): RoundResult = when {
            player == opponent -> RoundResult.DRAW
            player == Shape.ROCK && opponent == Shape.SCISSORS -> RoundResult.WIN
            player == Shape.PAPER && opponent == Shape.ROCK -> RoundResult.WIN
            player == Shape.SCISSORS && opponent == Shape.PAPER -> RoundResult.WIN
            else -> RoundResult.LOSS
        }
    }

    private enum class Shape(val score: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3),
    }

    private enum class RoundResult(val score: Int) {
        LOSS(0),
        DRAW(3),
        WIN(6),
    }
}

class Part2 {
    fun run() {
        val testGame = game("Day02_test")
        println(testGame.sumOf { it.score() })
    }

    private fun String.toShape() = when (this) {
        "A" -> Shape.ROCK
        "B" -> Shape.PAPER
        "C" -> Shape.SCISSORS
        else -> throw IllegalArgumentException("Unknown shape: $this")
    }

    private fun String.toResult() = when (this) {
        "X" -> RoundResult.LOSS
        "Y" -> RoundResult.DRAW
        "Z" -> RoundResult.WIN
        else -> throw IllegalArgumentException("Unknown result: $this")
    }

    private fun game(name: String): List<Round> {
        return readInput(name)
            .map {
                val (opponent, result) = it.split(" ")
                val player = result.toResult().playerShape(opponent.toShape())
                Round(player = player, opponent = opponent.toShape())
            }
    }

    private data class Round(val opponent: Shape, val player: Shape) {
        fun score(): Int = player.score + result().score

        private fun result(): RoundResult = when {
            player == opponent -> RoundResult.DRAW
            player == Shape.ROCK && opponent == Shape.SCISSORS -> RoundResult.WIN
            player == Shape.PAPER && opponent == Shape.ROCK -> RoundResult.WIN
            player == Shape.SCISSORS && opponent == Shape.PAPER -> RoundResult.WIN
            else -> RoundResult.LOSS
        }
    }

    private enum class Shape(val score: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3),
    }

    private enum class RoundResult(val score: Int) {
        LOSS(0),
        DRAW(3),
        WIN(6);

        fun playerShape(opponent: Shape): Shape = when (this) {
            RoundResult.DRAW -> opponent
            RoundResult.WIN -> when (opponent) {
                Shape.ROCK -> Shape.PAPER
                Shape.PAPER -> Shape.SCISSORS
                Shape.SCISSORS -> Shape.ROCK
            }

            RoundResult.LOSS -> when (opponent) {
                Shape.ROCK -> Shape.SCISSORS
                Shape.PAPER -> Shape.ROCK
                Shape.SCISSORS -> Shape.PAPER
            }
        }
    }
}

fun main() {
    Part1().run()
    Part2().run()
}
