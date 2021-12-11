fun main() {

    val CHUNK_MAP = mapOf(
        '{' to '}',
        '(' to ')',
        '[' to ']',
        '<' to '>',
    )

    val OPENING = setOf('{', '(', '[', '<')

    val CORRUPTION_SCORE = mapOf(
        '}' to 1197,
        ')' to 3,
        ']' to 57,
        '>' to 25137,
    )

    val COMPLETION_SCORE = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4,
    )

    fun checkProblem(line:String): D10Problem? {
        val stack = ArrayDeque<Char>()
        for (char in line) {
            if (char in OPENING) {
                stack.addFirst(char)
            } else if (stack.isEmpty()){
                return D10Corruption(char)
            } else if (CHUNK_MAP[stack.removeFirst()] != char){
                return D10Corruption(char)
            }
        }
        if (stack.isEmpty()) return null
        return D10Incomplete(CharArray(stack.size) { CHUNK_MAP[stack[it]]!!})
    }

    fun part1(input: List<String>): Int {
        return input.mapNotNull { checkProblem(it) }.filterIsInstance<D10Corruption>().sumOf { CORRUPTION_SCORE[it.char]!! }
    }

    fun part2(input: List<String>): Long {
        val scores = input.mapNotNull { checkProblem(it) }.filterIsInstance<D10Incomplete>().map { compeltion ->
            compeltion.line.fold(0L) {acc, current -> acc*5 + COMPLETION_SCORE[current]!! }
        }.sorted()
        return scores[scores.size/2]
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

sealed class D10Problem()

class D10Corruption(val char:Char) : D10Problem()
class D10Incomplete(val line:CharArray): D10Problem()
