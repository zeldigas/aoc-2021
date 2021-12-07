import kotlin.math.abs
import kotlin.math.ceil

fun main() {
    fun toModel(input: List<String>) = input[0].split(",").map { it.toInt() }.sorted()

    fun part1(input: List<String>): Int {
        val model = toModel(input)
        val midPoint = model.size / 2
        val median = model[midPoint]
        val fuel = model.sumOf { abs(it - median) }
        return fuel
    }

    fun progression(value: Int): Int {
        return (1 + value) * value / 2
    }

    fun part2(input: List<String>): Int {
        val model = toModel(input)
        val avg = ceil(model.sum().toDouble() / model.size.toDouble()).toInt()
        //rounding does not produce correct result, need to check both ints around
        val fuel = listOf(avg - 1, avg).map { point -> model.sumOf { progression(abs(it - point)) } }.minOf { it }
        return fuel
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))

}
