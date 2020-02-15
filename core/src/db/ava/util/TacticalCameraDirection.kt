package db.ava.util

import com.badlogic.gdx.math.MathUtils
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sign

const val FREE_INTERVAL = (PI / 10).toFloat()

sealed class TacticalCameraDirection {

    data class FREE(var rads: Float = 0f) : TacticalCameraDirection() {
        override fun cw(): TacticalCameraDirection {
            rads -= FREE_INTERVAL
            while (abs(rads) > 2 * PI) {
                rads -= (2 * PI * sign(rads)).toFloat()
            }
            return this
        }

        override fun ccw(): TacticalCameraDirection {
            rads += FREE_INTERVAL
            while (abs(rads) > 2 * PI) {
                rads -= (2 * PI * sign(rads)).toFloat()
            }
            return this
        }

        override fun direction(): Float { return rads }
    }

    class NORTH : TacticalCameraDirection() {
        override fun cw(): TacticalCameraDirection { return EAST() }
        override fun ccw(): TacticalCameraDirection { return WEST() }
        override fun direction(): Float { return MathUtils.PI * 3f / 2f }
    }

    class SOUTH : TacticalCameraDirection() {
        override fun cw(): TacticalCameraDirection { return WEST() }
        override fun ccw(): TacticalCameraDirection { return EAST() }
        override fun direction(): Float { return MathUtils.PI / 2f }
    }

    class EAST : TacticalCameraDirection() {
        override fun cw(): TacticalCameraDirection { return SOUTH() }
        override fun ccw(): TacticalCameraDirection { return NORTH() }
        override fun direction(): Float { return MathUtils.PI }
    }

    class WEST : TacticalCameraDirection() {
        override fun cw(): TacticalCameraDirection { return NORTH() }
        override fun ccw(): TacticalCameraDirection { return SOUTH() }
        override fun direction(): Float { return 0f }
    };

    abstract fun cw(): TacticalCameraDirection
    abstract fun ccw(): TacticalCameraDirection
    abstract fun direction(): Float
}