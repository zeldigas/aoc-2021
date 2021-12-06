fun main() {

    fun toModel(input: List<String>): D6Model {
        val registry = Array(9) { 0L }
        input[0].splitToSequence(",").map { it.toInt() }.forEach { registry[it]++ }
        val model = D6Model(registry)
        return model
    }

    fun part1(input: List<String>): Long {
        val model = toModel(input)

        repeat(80) { model.tick() }
        return model.total
    }


    fun part2(input: List<String>): Long {
        val model = toModel(input)

        repeat(256) { model.tick() }
        return model.total
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))

}

class D6Model(private val initialState: Array<Long>) {
    init {
        assert(initialState.size == 9)
    }

    fun tick() {
        val spawned = initialState.shiftLeft()
        initialState[6] += spawned
        initialState[8] += spawned
    }

    val total: Long
        get() = initialState.sum()
}

private fun Array<Long>.shiftLeft(): Long {
    val first = this[0]
    (0 until size - 1).forEach { this[it] = this[it + 1] }
    this[size - 1] = 0
    return first
}
