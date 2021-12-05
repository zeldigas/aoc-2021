fun main() {
    fun toCharArray(counter: Array<Int>, converter: (Int) -> Char) =
        counter.map(converter).toCharArray()

    fun toValue(counter: Array<Int>, converter: (Int) -> Char) =
        String(toCharArray(counter, converter)).toInt(2)

    fun collectStats(input: List<String>): Array<Int> {
        val counter = Array(input[0].length) { _ -> 0 }
        input.forEach { it.forEachIndexed { index, c -> if (c == '1') counter[index]++ } }
        return counter
    }

    fun part1(input: List<String>): Int {
        val boundary = input.size / 2
        val bitStats = collectStats(input)
        val gamma = toValue(bitStats) { if (it > boundary) '1' else '0' }
        val epsilon = toValue(bitStats) { if (it > boundary) '0' else '1' }
        return gamma * epsilon
    }


    fun part2(input: List<String>): Int {
        val tree: TreeNode = Mid()
        input.forEach { tree.add(it) }

        val oxygen = tree.find { zero, one -> if (one >= zero) '1' else '0'  }.toInt(2)
        val co2Scrubber = tree.find { zero, one -> if (zero <= one) '0' else '1'  }.toInt(2)


        return oxygen * co2Scrubber
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

sealed class TreeNode {
    abstract fun add(value: String) : TreeNode
    abstract fun find(selector:(Int, Int) -> Char): String
}

class Leaf(val ending:String) : TreeNode() {
    override fun add(value: String): TreeNode {
        return Mid().add(ending).add(value)
    }

    override fun find(selector:(Int, Int) -> Char): String {
        return ending
    }
}

class Mid(var one: TreeNode? = null, var zero: TreeNode? = null, var oneCounter:Int = 0, var zeroCounter:Int = 0): TreeNode() {
    override fun add(value: String): TreeNode {
        if (value[0] == '1') {
            one = one.addOrCreate(value)
            oneCounter++
        } else {
            zero = zero.addOrCreate(value)
            zeroCounter++
        }
        return this;
    }

    override fun find(selector:(Int, Int) -> Char): String {
        val selectedChar = selector(zeroCounter, oneCounter)
        val selected = if (selectedChar == '1') one else zero
        return selectedChar + selected!!.find(selector)
    }
}

private fun TreeNode?.addOrCreate(value: String): TreeNode = this?.add(value.substring(1)) ?: Leaf(value.substring(1))
