package db.ava.util

import kotlin.math.PI

const val DTR: Float = (PI / 180).toFloat()
const val RTD: Float = (180 / PI).toFloat()

fun radsToDegrees(rads: Float): Float {
    return rads * RTD
}

fun degreesToRads(degrees: Float): Float {
    return degrees * DTR
}