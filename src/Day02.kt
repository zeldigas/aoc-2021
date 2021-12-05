fun main() {
    fun toCommand(it: String): Command {
        val (type, value) = it.split(" ")
        return when (type) {
            "forward" -> Forward(value.toInt())
            "up" -> Depth(-value.toInt())
            "down" -> Depth(value.toInt())
            else -> throw UnsupportedOperationException("unknown type: $type")
        }
    }

    fun part1(input: List<String>): Int {
        return input.map { toCommand(it) }.fold(0 to 0) { current, event ->
            when (event) {
                is Depth -> (current.first + event.value) to current.second
                is Forward -> current.first to (current.second + event.value)
            }
        }.let { it.first * it.second }
    }


    fun part2(input: List<String>): Int {
        return input.map { toCommand(it) }
            .fold(State(0, 0, 0)) { state, event ->
                when (event) {
                    is Depth -> state.copy(aim = state.aim + event.value)
                    is Forward -> state.copy(
                        horizontal = state.horizontal + event.value,
                        depth = state.depth + (state.aim * event.value)
                    )
                }
            }.let { it.horizontal * it.depth }

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

sealed class Command(val value: Int)
class Depth(value: Int) : Command(value)
class Forward(value: Int) : Command(value)
data class State(val horizontal: Int, val depth: Int, val aim: Int)
