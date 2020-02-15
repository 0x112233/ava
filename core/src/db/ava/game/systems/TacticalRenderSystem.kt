package db.ava.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2
import db.ava.game.components.TacticalPositionComponent
import db.ava.game.components.TacticalRenderPriorityComponent
import db.ava.game.components.TacticalRenderableComponent
import db.ava.render.RenderStage
import db.ava.render.TacticalDecalGroupStrategy
import db.ava.render.TacticalRenderable
import db.ava.util.Blackboard
import db.ava.util.ComponentLookup
import db.ava.util.TacticalCameraDirection
import db.ava.util.radsToDegrees
import ktx.math.vec2


class TacticalRenderSystem : RenderStage, SortedIteratingSystem(
    Family.all(
        TacticalPositionComponent::class.java,
        TacticalRenderableComponent::class.java
    ).get(),
    { e1, e2 ->

        val point1 = ComponentLookup.get<TacticalPositionComponent>(e1)!!.point
        val point2 = ComponentLookup.get<TacticalPositionComponent>(e2)!!.point

        val axis = Blackboard.tacticalCameraDirection.direction()
        val axisNormal = vec2(1f, 0f).setAngle(radsToDegrees(axis))

        val pp1 = point1.x * axisNormal.x + point1.y * axisNormal.y
        val pp2 = point2.x * axisNormal.x + point2.y * axisNormal.y

        when {
            pp1 > pp2 -> 1
            pp1 < pp2 -> -1
            else -> {
                val prio1 = ComponentLookup.get<TacticalRenderPriorityComponent>(e1)?.priority ?: 0
                val prio2 = ComponentLookup.get<TacticalRenderPriorityComponent>(e2)?.priority ?: 0

                prio1 - prio2
            }
        }
    } //TODO: implement
) {

    val decalBatch = DecalBatch(TacticalDecalGroupStrategy())
    lateinit var tacticalFrameBuffer: FrameBuffer

    override fun update(deltaTime: Float) {
        forceSort()
        tacticalFrameBuffer.begin()

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT.or(GL20.GL_DEPTH_BUFFER_BIT))

        super.update(deltaTime)
        decalBatch.flush()
        tacticalFrameBuffer.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val renderable = ComponentLookup.get<TacticalRenderableComponent>(entity)!!

        when (renderable.tacticalRenderable) {
            is TacticalRenderable.Billboard -> decalBatch.add((renderable.tacticalRenderable as TacticalRenderable.Billboard).decal)
            is TacticalRenderable.CrossBillboard -> (renderable.tacticalRenderable as TacticalRenderable.CrossBillboard).decals.forEach { decalBatch.add(it) }
            is TacticalRenderable.Tile -> decalBatch.add((renderable.tacticalRenderable as TacticalRenderable.Tile).decal)
            is TacticalRenderable.TileStack -> (renderable.tacticalRenderable as TacticalRenderable.TileStack).decals.forEach { decalBatch.add(it) }
        }
    }

    override fun resize(width: Int, height: Int) {
        tacticalFrameBuffer = FrameBuffer(Pixmap.Format.RGBA8888, width, height, true)
    }

    override fun getFrameBuffer(): FrameBuffer {
        return tacticalFrameBuffer
    }
}