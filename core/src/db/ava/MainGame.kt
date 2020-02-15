package db.ava

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import db.ava.game.TacticalLayer
import db.ava.game.components.TacticalPositionComponent
import db.ava.game.components.TacticalRenderPriorityComponent
import db.ava.game.components.TacticalRenderableComponent
import db.ava.util.Blackboard
import db.ava.util.ComponentLookup

class MainGame : ApplicationAdapter() {

    val tacticalLayer = TacticalLayer()

    override fun create() {
        Blackboard.renderPipeline.init()
        tacticalLayer.initialize()

        ComponentLookup.register<TacticalPositionComponent>()
        ComponentLookup.register<TacticalRenderableComponent>()
        ComponentLookup.register<TacticalRenderPriorityComponent>()
    }

    override fun render() {
        val dt = Gdx.graphics.deltaTime
        tacticalLayer.update(dt)
        tacticalLayer.render()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        Blackboard.renderPipeline.resize(width, height)
    }

    override fun dispose() {
    }
}