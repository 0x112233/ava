package db.ava.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import db.ava.game.components.TacticalPositionComponent
import db.ava.game.components.TacticalRenderableComponent
import db.ava.render.TacticalRenderable
import db.ava.util.Blackboard
import db.ava.util.ComponentLookup

const val TILES_IN_STACK = 6 // Keep this even!
const val TILES_IN_HALF_STACK = TILES_IN_STACK / 2
const val TILE_STACK_HEIGHT = 1f
const val TILE_STACK_DISTANCE = TILE_STACK_HEIGHT / TILES_IN_STACK

class TacticalRenderableTransformSystem : IteratingSystem(
    Family.all(
        TacticalPositionComponent::class.java,
        TacticalRenderableComponent::class.java
    ).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val pos = ComponentLookup.get<TacticalPositionComponent>(entity)!!.point
        val renderable = ComponentLookup.get<TacticalRenderableComponent>(entity)!!

        when (renderable.tacticalRenderable) {
            is TacticalRenderable.Tile -> {
                val tile = (renderable.tacticalRenderable as TacticalRenderable.Tile).decal
                tile.position.set(pos.x.toFloat(), 0f, pos.y.toFloat())
            }
            is TacticalRenderable.TileStack -> {
                (renderable.tacticalRenderable as TacticalRenderable.TileStack).decals.forEachIndexed { index, tile ->
                    tile.position.set(pos.x.toFloat(), index.toFloat() * TILE_STACK_DISTANCE, pos.y.toFloat())
                }
            }
            is TacticalRenderable.Billboard -> {
                val bb = (renderable.tacticalRenderable as TacticalRenderable.Billboard).decal
                bb.position.set(pos.x.toFloat(), 0.5f, pos.y.toFloat())
                Blackboard.tacticalCamera.aimBillBoard(bb)
            }
        }
    }
}