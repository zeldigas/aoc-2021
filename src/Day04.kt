fun main() {

    fun toRow(line: String) = line.split(" ").filter { it.isNotEmpty() }

    fun readBoards(input: List<String>): List<Board> {
        val boardSize = toRow(input[2]).size
        val boards =
            input.subList(2, input.size).filter { it.isNotEmpty() }.windowed(boardSize, step = boardSize).map { lines ->
                Board.create(
                    lines.map { line -> toRow(line).map { it.toInt() } }
                )
            }
        return boards
    }

    fun part1(input: List<String>): Int {
        val draws = input[0].split(",").map { it.toInt() }
        val boards = readBoards(input)

        val number = draws.first { draw -> boards.any { it.draw(draw) } }
        return number * boards.first { it.won }.leftOvers
    }


    fun part2(input: List<String>): Int {
        val draws = input[0].split(",").map { it.toInt() }
        var boards = readBoards(input)

        var lastNumber = 0
        var lastBoard: Board = boards[0]

        for (number in draws) {
            val boardsThatWon = boards.map {
                it.draw(number)
                it
            }.filter { it.won }

            if (boardsThatWon.isNotEmpty()) {
                println("Draw: $number, Boards that won: $boardsThatWon")
                lastNumber = number
                lastBoard = boardsThatWon.last()
            }

            boards = boards - boardsThatWon.toSet()
        }

        return lastNumber * lastBoard.leftOvers
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

data class Board(private var lines: List<List<Item>>) {

    companion object {
        fun create(boardLines: List<List<Int>>): Board = Board(boardLines.map { line -> line.map { Unmarked(it) } })
    }

    fun draw(value: Int): Boolean {
        lines = lines.map { line ->
            line.map {
                when (it) {
                    is Marked -> it
                    is Unmarked -> if (it.number == value) Marked(value) else it
                }
            }
        }
        return won
    }

    val won: Boolean
        get() = lines.any { it.marked() } || lines.transpose().any { it.marked() }

    val leftOvers: Int
        get() = lines.sumOf { it.filterIsInstance<Unmarked>().sumOf { it.number } }
}

private fun List<Item>.marked() = all { it is Marked }

sealed class Item(val number: Int)
class Marked(number: Int) : Item(number) {
    override fun toString(): String {
        return "*$number"
    }
}

class Unmarked(number: Int) : Item(number) {
    override fun toString(): String {
        return "$number"
    }
}