import Day07.Back
import Day07.ChangeDirectory
import Day07.Directory
import Day07.File
import Day07.Instruction
import Day07.ListFiles

class Day07 {
    sealed interface Instruction

    object Back : Instruction
    data class ChangeDirectory(val toDir: String) : Instruction
    object ListFiles : Instruction
    data class File(val name: String, val size: Int) : Instruction
    data class Directory(val name: String) : Instruction
}

fun main() {
    data class DirectoryDepth(val name: String, val depth: Int, val number: Int)

    fun String.toInstruction(): Instruction = when {
        this == "$ cd .." -> Back
        this.startsWith("$ cd") -> ChangeDirectory(toDir = this.substringAfter("$ cd "))
        this == "$ ls" -> ListFiles
        this.startsWith("dir") -> Directory(name = this.substringAfter("dir "))
        else -> {
            val (size, name) = this.split(" ")
            File(name = name, size = size.toInt())
        }
    }

    class DirectoriesStructure(
        val directoriesTree: Map<DirectoryDepth, List<DirectoryDepth>>,
        val directoriesFiles: Map<DirectoryDepth, List<File>>
    ) {

        fun allDirectories(): List<DirectoryDepth> =
            directoriesTree.flatMap { (parentDir, dirs) -> dirs + parentDir }.distinct()
                .apply { println(this.size) }

        fun part1Result(): Int = allDirectories()
            .map { dir -> dir to dir.size() }
            .filter { (dir, size) ->
                size <= 100_000 && dir.name != "/"
            }
            .onEach { println("size: " + it.second) }
            .sumOf { (_, size) -> size }

        fun totalSize(): Int = allDirectories().sumOf { it.size() }

        fun dirsBySizes(): List<Pair<DirectoryDepth, Int>> = allDirectories().map { it to it.size() }

        private fun DirectoryDepth.size(): Int {
            val filesSize = directoriesFiles[this]?.sumOf { it.size } ?: 0
            val directories = directoriesTree[this] ?: emptyList()
            if (directories.isEmpty()) {
                return filesSize
            }
            val directoriesSize = directories.sumOf { it.size() }
            return filesSize + directoriesSize
        }
    }

    fun List<Instruction>.toDirectoriesStructure(): DirectoriesStructure {
        val directoriesTree = mutableMapOf<DirectoryDepth, List<DirectoryDepth>>()
        val directoriesFiles = mutableMapOf<DirectoryDepth, List<File>>()

        val directoriesStack = ArrayDeque<DirectoryDepth>()
        val visitedDirsOccurrences = mutableMapOf<Pair<String, Int>, Int>()

        fun number(name: String, depth: Int): Int = (visitedDirsOccurrences[name to depth] ?: 0)

        this.forEach { instruction ->
            when (instruction) {
                is Directory -> {
                    visitedDirsOccurrences.merge(
                        instruction.name to directoriesStack.size + 1,
                        1
                    ) { o1, o2 -> o1 + o2 }
                    directoriesTree.merge(
                        directoriesStack.last(),
                        listOf(
                            DirectoryDepth(
                                name = instruction.name,
                                depth = directoriesStack.size + 1,
                                number = number(instruction.name, directoriesStack.size + 1)
                            )
                        )
                    ) { d1, d2 -> d1 + d2 }
                }

                is File -> {
                    directoriesFiles
                    directoriesFiles.merge(directoriesStack.last(), listOf(instruction)) { f1, f2 -> f1 + f2 }
                }

                is Back -> {
                    directoriesStack.removeLast()
                }

                is ChangeDirectory -> {
                    directoriesStack.addLast(
                        DirectoryDepth(
                            name = instruction.toDir,
                            depth = directoriesStack.size + 1,
                            number = number(instruction.toDir, directoriesStack.size + 1)
                        )
                    )
                }

                is ListFiles -> {}
            }
        }

        return DirectoriesStructure(directoriesTree = directoriesTree, directoriesFiles = directoriesFiles)
    }

    val (_, occurrences) = readInput("Day07_test")
        .filter { it.startsWith("$ cd") }
        .fold(0 to emptyList<Int>()) { (depth, occurrences), cd ->
            when (cd) {
                "$ cd .." -> depth - 1 to occurrences
                "$ cd gqlg" -> depth + 1 to occurrences + (depth + 1)
                else -> depth + 1 to occurrences
            }
        }
    println(occurrences)

    val part1DirectoriesStructure = readInput("Day07_test")
        .map { it.toInstruction() }
        .toDirectoriesStructure()
    val part1Result = part1DirectoriesStructure
        .part1Result()

    println(part1Result)
    check(part1Result == 1749646)

    val totalDiskSpace = 70_000_000
    val neededForUpdate = 30_000_000
    // 221035442
    // 41412830
    val usedDiskSpace = part1DirectoriesStructure.totalSize() // 1498966
    val atLeastToDelete = neededForUpdate - (totalDiskSpace - usedDiskSpace)

    check(totalDiskSpace > usedDiskSpace)
    part1DirectoriesStructure.dirsBySizes()
        .filter { (_, size) -> size >= atLeastToDelete }
        .minBy { (_, size) -> size }
        .apply { println("2nd part: $this") }
}
