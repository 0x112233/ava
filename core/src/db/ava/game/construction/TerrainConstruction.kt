package db.ava.game.construction

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.decals.Decal
import com.badlogic.gdx.math.Vector3
import db.ava.game.components.TacticalPositionComponent
import db.ava.game.components.TacticalRenderPriorityComponent
import db.ava.game.components.TacticalRenderableComponent
import db.ava.game.systems.TILES_IN_HALF_STACK
import db.ava.game.systems.TILES_IN_STACK
import db.ava.render.TacticalRenderable
import db.ava.util.FLOOR_PRIORITY
import db.ava.util.entity
import db.ava.world.Point
import ktx.collections.GdxArray


fun constructStackedWall(
    engine: PooledEngine,
    region: TextureRegion,
    lowColor: Color,
    topColor: Color,
    point: Point
) {
    engine.entity {
        with<TacticalPositionComponent> {
            this.point.set(point)
        }
        with<TacticalRenderableComponent> {
            val decals = GdxArray<Decal>(TILES_IN_STACK)
            for (i in 0 until TILES_IN_STACK) {
                val colorToUse = when (i) {
                    TILES_IN_STACK - 1 -> topColor
                    else -> lowColor
                }
                val decal = Decal.newDecal(1f, 1f, region)
                decal.setPackedColor(colorToUse.toFloatBits())
                decal.setRotation(Vector3(0f, 1f, 0f), Vector3(0f, 0f, 1f))
                decals.add(decal)
            }
            tacticalRenderable = TacticalRenderable.TileStack(decals)
        }
        // TODO: collider component
    }
}

fun constructStackedShortWall(
    engine: PooledEngine,
    region: TextureRegion,
    lowColor: Color,
    topColor: Color,
    point: Point
) {
    engine.entity {
        with<TacticalPositionComponent> {
            this.point.set(point)
        }
        with<TacticalRenderableComponent> {
            val decals = GdxArray<Decal>(TILES_IN_HALF_STACK)
            for (i in 0 until TILES_IN_HALF_STACK) {
                val colorToUse = when (i) {
                    TILES_IN_HALF_STACK - 1 -> topColor
                    else -> lowColor
                }
                val decal = Decal.newDecal(1f, 1f, region)
                decal.setPackedColor(colorToUse.toFloatBits())
                decal.setRotation(Vector3(0f, 1f, 0f), Vector3(0f, 0f, 1f))
                decals.add(decal)
            }
            tacticalRenderable = TacticalRenderable.TileStack(decals)
        }
        // TODO: collider component
    }
}


fun constructBillboardTerrain(
    engine: PooledEngine,
    region: TextureRegion,
    color: Color,
    point: Point
) {
    engine.entity {
        with<TacticalPositionComponent> {
            this.point.set(point)
        }
        with<TacticalRenderableComponent> {
            val decal = Decal.newDecal(1f, 1f, region)
            decal.setPackedColor(color.toFloatBits())
            tacticalRenderable = TacticalRenderable.Billboard(decal)
        }
    }
}

fun constructFloor(
    engine: PooledEngine,
    region: TextureRegion,
    color: Color,
    point: Point
) {
    engine.entity {
        with<TacticalPositionComponent> {
            this.point.set(point)
        }
        with<TacticalRenderableComponent> {
            val decal = Decal.newDecal(1f, 1f, region)
            decal.setPackedColor(color.toFloatBits())
            decal.setRotation(Vector3(0f, 1f, 0f), Vector3(0f, 0f, 1f))
            tacticalRenderable = TacticalRenderable.Tile(decal)
        }
        with<TacticalRenderPriorityComponent> {
            priority = FLOOR_PRIORITY
        }
    }
}