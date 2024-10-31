package org.example

fun nextGeneration(grid: Array<Array<Boolean>>): Array<Array<Boolean>>{
    val newGrid =  Array(grid.size) { Array(grid[0].size) { false } }
    for (x in grid.indices){
        for (y in grid[x].indices){
            val liveNeighbors = countLiveNeighbors(grid, x, y)

            newGrid[x][y] = if (grid[x][y]) {
                liveNeighbors in 2..3
            } else {
                liveNeighbors == 3
            }
        }
    }
    return newGrid
}

fun countLiveNeighbors(grid: Array<Array<Boolean>>, x: Int, y: Int): Int{
    var count = 0
    for (i in -1..1){
        for (j in -1..1){
            if (i == 0 && j == 0) continue
            val nx = x + i
            val ny = y + j
            if (nx in grid.indices && ny in grid[0].indices)
                count += if (grid[nx][ny]) 1 else 0
        }
    }
    return count
}
