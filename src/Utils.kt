import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Transpose lines matrix
 */
fun <T> List<List<T>>.transpose(): List<List<T>> {
    return (0 until size).map { index -> this.map { it[index] } }
}

typealias Matrix2D = Array<Array<Int>>

fun matrix(x: Int, y: Int): Matrix2D {
    return Array(y) { Array(x) { 0 } }
}

fun Matrix2D.get(x:Int, y: Int) = this[y][x]
fun Matrix2D.inc(x:Int, y: Int) = this[y][x]++
fun Matrix2D.set(x:Int, y: Int, value: Int) {
    this[y][x] = value
}

fun matrix(sizeX: Int, sizeY: Int, source: Matrix2D): Matrix2D {
    val sourceSizeX = source[0].size
    val sourceSizeY = source.size
    return Array(sizeY) { y -> Array(sizeX) { x -> if (x < sourceSizeX && y < sourceSizeY) source[y][x] else 0 } }
}
