fun main() {
    fun toModel(input: List<String>) = input[0].split(",").map { it.toInt() }.sorted()

    fun part1(input: List<String>): Int {
        return input.map {
            it.split("|")[1].trim().split(" ")
                .filter { it.length in setOf(2, 4, 3, 7).toList() }
        }.sumOf { it.size }
    }

    fun Map<Int, Set<Char>>.union(vararg number: Int): Set<Char> {
        return number.fold(emptySet()) { current, num -> current + this[num]!! }
    }

    fun <T> Collection<T>.singleResult(predicate:(T) -> Boolean): T {
        val filtered = filter(predicate)
        check(filtered.size == 1) { "Expected single result but got: $filtered"}
        return filtered.first()
    }

    fun MutableMap<Int, Set<Char>>.reverse() =
        map { (k, v) -> v to k }.toMap()

    fun decodeNumberInput(encodedNumbers: List<Set<Char>>): Map<Set<Char>, Int> {
        val numberMap = mutableMapOf(
            1 to encodedNumbers.singleResult { it.size == 2 },
            4 to encodedNumbers.singleResult { it.size == 4 },
            7 to encodedNumbers.singleResult { it.size == 3 },
            8 to encodedNumbers.singleResult { it.size == 7 }
        )
        val fourAndSeven = numberMap.union(4, 7)
        numberMap[9] = encodedNumbers.singleResult { it.size == 6 && it.containsAll(fourAndSeven) }
        numberMap[5] = encodedNumbers.singleResult { it.size == 5 && numberMap[9]!!.containsAll(it) && (numberMap[1]!! - it).isNotEmpty()}
        numberMap[6] = encodedNumbers.singleResult { it.size == 6 && it != numberMap[9] && it.containsAll(numberMap[5]!!) }
        numberMap[0] = encodedNumbers.singleResult { it.size == 6 && it !in numberMap.values }
        val dotMap = mutableMapOf(
            'a' to (numberMap[7]!! - numberMap[1]!!).single(),
            'g' to (numberMap[9]!! - fourAndSeven).single(),
            'd' to (numberMap[4]!! - numberMap[0]!!).single()
        )
        numberMap[3] = encodedNumbers.single { it.size == 5 && it == (numberMap[1]!! + setOf(dotMap['a']!!, dotMap['d']!!, dotMap['g']!!))}
        numberMap[2] = encodedNumbers.single { it !in numberMap.values}

        return numberMap.reverse()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            val (numberInput, task) = it.split("|")
            val numbers = decodeNumberInput(numberInput.trim().split(" ").map { it.toSet() })
            task.trim().split(" ").map { encodedNumber -> numbers[encodedNumber.toSet()] }.joinToString("").toInt()
        }.sumOf { it }
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
