package db.ava.render

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.decals.Decal
import com.badlogic.gdx.math.MathUtils.PI
import com.badlogic.gdx.math.MathUtils.cos
import com.badlogic.gdx.math.Matrix4
import db.ava.util.TacticalCameraDirection
import db.ava.world.Point
import kotlin.math.atan
import kotlin.math.sin
import kotlin.math.sqrt


private const val CAMERA_ROTATION_OFFSET = 0.1f
private val CAMERA_HEIGHT = 5f
private val CAMERA_DISTANCE = CAMERA_HEIGHT * sqrt(3f)


class TacticalCamera {
    private val camera = PerspectiveCamera()
    val target = Point(0, 0)

    init {
        compose(TacticalCameraDirection.NORTH())
    }

    fun aimBillBoard(bb: Decal) {
        bb.lookAt(camera.position, camera.up)
    }

    fun compose(direction: TacticalCameraDirection) {
        //camera.setToOrtho(false, 16f, 9f)
        camera.viewportWidth = 16f
        camera.viewportHeight = 9f
        camera.fieldOfView = 67f
        camera.near = 0.1f
        camera.far = 100f

        val directionRadians = direction.direction() + CAMERA_ROTATION_OFFSET
        val cameraX = CAMERA_DISTANCE * cos(directionRadians) + target.x
        val cameraZ = CAMERA_DISTANCE * sin(directionRadians) + target.y
        val cameraY = CAMERA_HEIGHT

//        camera.position.set(cameraX, cameraY, cameraZ)
//        camera.lookAt(target.x.toFloat(), 0f, target.y.toFloat())
        camera.position.set(cameraX, cameraY, cameraZ)
        camera.up.set(0f, 1f, 0f)
        camera.lookAt(target.x.toFloat(), 0f, target.y.toFloat())
        camera.update()
    }

    fun combined() : Matrix4 {
        return camera.combined!!
    }


}