package db.ava.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import db.ava.world.Point

class TacticalPositionComponent : Component, Pool.Poolable {
    val point = Point(0, 0)

    override fun reset() {
        point.set(0, 0)
    }
}