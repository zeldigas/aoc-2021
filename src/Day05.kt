import kotlin.math.abs
import kotlin.math.max

//using non-sparse matrix as amount of data and matrix dimension is not that large
fun main() {

    fun readBoard(input: List<String>, lineFilter: (Line) -> Boolean = {true}): LineBoard {
        val lines = input.map { it.toLine() }.filter(lineFilter)
        val firstLine = lines[0]
        val initial = LineBoard(matrix(firstLine.maxX+1, firstLine.maxY+1)).register(firstLine)
        return lines.subList(1, lines.size).fold(initial) { board, line -> board.register(line) }
    }

    fun part1(input: List<String>): Int {
        val board = readBoard(input) { !it.diagonal }

        return board.count { it > 1 }
    }


    fun part2(input: List<String>): Int {
        val board = readBoard(input)

        return board.count { it > 1 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

private fun String.toLine(): Line {
    val (start, end) = split(" -> ").map { it.toPoint() }.sortedWith(Point.comparator)
    return Line(start, end)
}

private fun String.toPoint(): Point {
    val (x, y) = split(",")
    return Point(x.toInt(), y.toInt())
}

data class Point(val x: Int, val y: Int) : Comparable<Point> {
    companion object {
        val comparator: Comparator<Point> = compareBy<Point> { it.x }.thenComparingInt { it.y }
    }

    override fun compareTo(other: Point): Int {
        return comparator.compare(this, other)
    }
}

class Line(private val start: Point, private val end: Point) {
    val maxX: Int
        get() = max(start.x, end.x)
    val maxY: Int
        get() = max(start.y, end.y)

    val points: List<Point>
        get() {
            return when {
                start.x == end.x -> (start.y..end.y).map { Point(start.x, it) }
                start.y == end.y -> (start.x..end.x).map { Point(it, start.y) }
                diagonal -> {
                    val direction = if( start.y - end.y > 0) -1 else 1
                    val count = end.x - start.x
                    (0 .. count).map { Point(start.x+it, start.y+direction*it) }
                }
                else -> throw IllegalStateException("Unknown type of line $this")
            }
        }

    val diagonal: Boolean
        get() = abs(start.x - end.x) == abs(start.y - end.y)

    override fun toString(): String {
        return "[$start, $end]"
    }


}

class LineBoard(private val field: Matrix2D) {

    private val sizeX: Int = field[0].size
    private val sizeY: Int = field.size

    fun register(line: Line): LineBoard {
        return if (line.maxX >= sizeX || line.maxY >= sizeY) {
            val newBoard = LineBoard(matrix(max(line.maxX+1, sizeX), max(line.maxY+1, sizeY), field))
            newBoard.register(line)
        } else {
            registerPoints(line.points)
            this
        }
    }

    private fun registerPoints(points: List<Point>) {
        for ((x, y) in points) {
            field.inc(x, y)
        }
    }

    fun count(predicate: (Int) -> Boolean) = field.sumOf { line -> line.sumOf {
        val result:Int = if (predicate(it)) 1 else 0
        result
    } }

    override fun toString(): String {
        return field.joinToString("\n") { it.toList().toString() }
    }


}