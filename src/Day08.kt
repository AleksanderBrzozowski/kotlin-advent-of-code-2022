import kotlin.math.max

fun main() {
    fun part1() {
        val rows = readInput("Day08_test")
        val columnsSize = rows[0].length

        fun leftTress(rowIndex: Int, columnIndex: Int): List<Int> =
            (0 until columnIndex).map { rows[rowIndex][it].digitToInt() }

        fun rightTress(rowIndex: Int, columnIndex: Int): List<Int> =
            ((columnIndex + 1) until columnsSize).map { rows[rowIndex][it].digitToInt() }

        fun topTress(rowIndex: Int, columnIndex: Int): List<Int> =
            (0 until rowIndex).map { rows[it][columnIndex].digitToInt() }

        fun bottomTress(rowIndex: Int, columnIndex: Int): List<Int> =
            ((rowIndex + 1) until rows.size).map { rows[it][columnIndex].digitToInt() }

        var sum = (rows.size * 2) + (columnsSize - 2) * 2
        rows.forEachIndexed { i, columns ->
            columns.forEachIndexed { j, height ->
                if (i == 0 || j == 0 || i == (rows.size - 1) || j == (columns.length - 1)) {
                    return@forEachIndexed
                }
                val listOf = listOf(
                    leftTress(i, j).all { it < height.digitToInt() },
                    rightTress(i, j).all { it < height.digitToInt() },
                    topTress(i, j).all { it < height.digitToInt() },
                    bottomTress(i, j).all { it < height.digitToInt() }
                )
                val visible = listOf.any { it }
                if (visible) {
                    sum += 1
                }
            }
        }

        println(sum)
    }

    fun part2() {
        val rows = readInput("Day08_test")
        val columnsSize = rows[0].length

        fun leftTrees(rowIndex: Int, columnIndex: Int): List<Int> =
            (0 until columnIndex).map { rows[rowIndex][it].digitToInt() }

        fun rightTrees(rowIndex: Int, columnIndex: Int): List<Int> =
            ((columnIndex + 1) until columnsSize).map { rows[rowIndex][it].digitToInt() }

        fun topTrees(rowIndex: Int, columnIndex: Int): List<Int> =
            (0 until rowIndex).map { rows[it][columnIndex].digitToInt() }

        fun bottomTrees(rowIndex: Int, columnIndex: Int): List<Int> =
            ((rowIndex + 1) until rows.size).map { rows[it][columnIndex].digitToInt() }

        fun List<Int>.countVisibleTrees(height: Char): Int = fold(0) { visibleTreesSum, treeHeight ->
            when {
                height.digitToInt() == treeHeight -> return visibleTreesSum + 1
                height.digitToInt() > treeHeight -> visibleTreesSum + 1
                else -> return visibleTreesSum + 1
            }
        }

        var highestScenicScore = 0
        rows.forEachIndexed { i, columns ->
            columns.forEachIndexed { j, height ->
                if (i == 0 || j == 0 || i == (rows.size - 1) || j == (columns.length - 1)) {
                    return@forEachIndexed
                }
                val leftTrees = leftTrees(i, j).reversed()
                val rightTrees = rightTrees(i, j)
                val topTrees = topTrees(i, j).reversed()
                val bottomTrees = bottomTrees(i, j)
                val visibleTreesInEachDirection = listOf(
                    leftTrees.countVisibleTrees(height),
                    rightTrees.countVisibleTrees(height),
                    topTrees.countVisibleTrees(height),
                    bottomTrees.countVisibleTrees(height)
                )
                val scenicScore = visibleTreesInEachDirection
                    .fold(1) { visibleTrees, score ->
                        visibleTrees * score
                    }
                highestScenicScore = max(highestScenicScore, scenicScore)
            }
        }

        println(highestScenicScore)
    }

    part1()
    part2()
}
