package db.ava.game.systems

// Updated in real time
interface ContinuousSystem {
    fun update(dt: Float)
}