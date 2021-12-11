fun main() {
    fun toModel(input: List<String>) = input.map { it.toCharArray().map { "$it".toInt() }.toIntArray() }.toTypedArray()

    fun Array<IntArray>.valid(point: Point):Boolean {
        return point.x in (indices) && point.y in this[point.x].indices
    }

    fun Array<IntArray>.valueAt(point: Point):Int {
        return this[point.x][point.y]
    }

    fun Array<IntArray>.valueAt(row:Int, column:Int, predicate:(Int) -> Boolean): Boolean {
        return !valid(Point(row, column)) || predicate(this[row][column])
    }

    fun lowPoint(row:Int, col:Int, value:Int, map: Array<IntArray>): Boolean {
        val predicate: (Int) -> Boolean = { it > value}
        return map.valueAt(row - 1, col, predicate)
                && map.valueAt(row + 1, col, predicate)
                && map.valueAt(row , col - 1, predicate)
                && map.valueAt(row , col + 1, predicate)
    }

    fun part1(input: List<String>): Int {
        val map = toModel(input)
        return map.asSequence().flatMapIndexed {
                rowIndex, row -> row.filterIndexed { colIndex, value -> lowPoint(rowIndex, colIndex, value, map)}
        }.map { it + 1 }.sum()
    }

    fun basinSize(map:Array<IntArray>, row:Int, col:Int) : Int {
        val points = mutableSetOf<Point>()
        fun traverse(points: MutableSet<Point>,map: Array<IntArray>, point:Point) {
            if (point in points || !map.valid(point)) return
            val value = map.valueAt(point)
            if (value == 9) return

            points.add(point)
            val (pointRow, pointCol) = point
            traverse(points, map, Point(pointRow - 1, pointCol))
            traverse(points, map, Point(pointRow + 1, pointCol))
            traverse(points, map, Point(pointRow, pointCol - 1))
            traverse(points, map, Point(pointRow, pointCol + 1))
        }
        traverse(points, map, Point(row, col))

        return points.size
    }

    fun part2(input: List<String>): Int {
        val map = toModel(input)
        val basins = map.asSequence().flatMapIndexed {
                rowIndex, row -> row.indices.map { rowIndex to it }.filter { (rowIndex, colIndex) -> lowPoint(rowIndex, colIndex, map[rowIndex][colIndex], map)}
        }.map { (row, col) -> basinSize(map, row, col) }.sortedDescending().take(3)

        return basins.reduce { acc, i -> acc*i }
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
