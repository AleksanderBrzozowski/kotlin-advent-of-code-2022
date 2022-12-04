import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun <T> List<T>.replaceLast(value: T): List<T> = dropLast(1) + value

fun <T> List<T>.replaceLast(block: (T) -> T): List<T> {
    val last = last()
    return dropLast(1) + block(last)
}
