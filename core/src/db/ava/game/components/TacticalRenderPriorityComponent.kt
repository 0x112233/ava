package db.ava.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

class TacticalRenderPriorityComponent : Component, Pool.Poolable {
    var priority: Int = 0

    override fun reset() {
        priority = 0
    }
}