package db.ava.util

import com.badlogic.ashley.core.PooledEngine
import db.ava.render.RenderPipeline
import db.ava.render.TacticalCamera

object Blackboard {
    val engine = PooledEngine()
    val renderPipeline = RenderPipeline()

    var tacticalCameraDirection: TacticalCameraDirection = TacticalCameraDirection.SOUTH()
    val tacticalCamera = TacticalCamera()

}