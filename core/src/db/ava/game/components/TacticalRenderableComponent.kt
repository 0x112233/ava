package db.ava.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import db.ava.render.TacticalRenderable

class TacticalRenderableComponent : Component, Pool.Poolable {
    var tacticalRenderable: TacticalRenderable? = null

    override fun reset() {
        tacticalRenderable = null
    }
}