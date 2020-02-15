package db.ava.game.message

interface ActionAnimation {
    // TODO: implement

    // Return how long the animation should last
    fun totalDuration() : Float

    // Apply the animation for the given t
    // t will be in [0, 1]
    fun animate(t: Float)

    // Clean up any modified global state
    fun cleanup()
}