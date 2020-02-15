package db.ava.world

data class Point(var x: Int, var y: Int) {
    fun set(other: Point) {
        x = other.x
        y = other.y
    }

    fun set(x: Int, y: Int) {
        this.x = x
        this.y = y
    }
}