package db.ava.game

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.decals.Decal
import com.badlogic.gdx.math.Vector3
import db.WorldAction
import db.ava.game.components.TacticalPositionComponent
import db.ava.game.components.TacticalRenderPriorityComponent
import db.ava.game.components.TacticalRenderableComponent
import db.ava.game.construction.constructBillboardTerrain
import db.ava.game.construction.constructFloor
import db.ava.game.construction.constructStackedShortWall
import db.ava.game.construction.constructStackedWall
import db.ava.game.message.WorldEvent
import db.ava.game.message.event.TimePassedEvent
import db.ava.game.systems.ReactiveSystem
import db.ava.game.systems.TILES_IN_STACK
import db.ava.game.systems.TacticalRenderSystem
import db.ava.game.systems.TacticalRenderableTransformSystem
import db.ava.render.TacticalRenderable
import db.ava.util.*
import db.ava.world.Point
import ktx.collections.GdxArray
import kotlin.random.Random

class TacticalLayer {

    private sealed class TacticalState {
        data class ANIMATING_ACTION(val action: WorldAction, var timeTaken: Float) : TacticalState()
        data class ENTITY_ACTING(val entity: Entity) : TacticalState()
        data class EXECUTING_ACTION(val action: WorldAction) : TacticalState()
        class SELECTING_NEXT_ACTION: TacticalState()
        class SELECTING_NEXT_ENTITY: TacticalState()
        class WAITING_FOR_PLAYER : TacticalState()
    }

    private var currentState: TacticalState = TacticalState.SELECTING_NEXT_ACTION()
    private var lastState: TacticalState = TacticalState.SELECTING_NEXT_ACTION()

    val actionQueue = GdxQueue<WorldAction>()
    val reactiveSystems = GdxArray<ReactiveSystem>()
    val scheduler = Scheduler()

    fun tryAction(action: WorldAction) : Boolean {
        return reactiveSystems.fold(true) {acc, sys -> acc && sys.allowAction(action)}
    }

    fun processEvent(event: WorldEvent) : GdxArray<WorldAction> {
        return reactiveSystems.fold(GdxArray()) {acc, sys -> acc.addAll(sys.onEvent(event)); acc }
    }

    fun processEventAndEnqueueReactions(event: WorldEvent) {
        processEvent(event).forEach {
            actionQueue.enqueue(it)
        }
    }

    fun initialize() {
        val tacticalRenderSystem = TacticalRenderSystem()

        Blackboard.engine.addSystem(TacticalRenderableTransformSystem())
        Blackboard.engine.addSystem(tacticalRenderSystem)

        Blackboard.renderPipeline.addStage(tacticalRenderSystem)

        val texture = Texture("assets/monochrome_transparent.png")
        val regions = GdxArray<TextureRegion>()

        regions.add(TextureRegion(texture, 16 * 17, 0, 16, 16)) // stack
        regions.add(TextureRegion(texture, 0, 17, 16, 16)) // tree

        for (i in 1..7) {
            regions.add(TextureRegion(texture, i * 17, 0, 16, 16))
        }

        val rand = Random(0)

        val width = 3

        for (i in 0..(width + 1) * 2) {
            Blackboard.engine.entity {
                with<TacticalPositionComponent> {
                    point.x = width
                    point.y = i - width
                }
                with<TacticalRenderableComponent> {
                    val decal = Decal.newDecal(1f, 1f, TextureRegion(texture, 30 * 17, 0, 16, 16))
                    tacticalRenderable = TacticalRenderable.Billboard(decal)
                }
                with<TacticalRenderPriorityComponent> {
                    priority = ACTOR_PRIORITY
                }
            }
        }

        val stackTopColor = Color.GRAY
        val stackLowColor = Color.DARK_GRAY

        val treeColor = Color.GREEN

        val floorColor = Color.DARK_GRAY

        for (i in 0..width * 2) {
            for (j in 0..width * 2) {
                val regionIndex = rand.nextInt(regions.size)
                val region = regions[regionIndex]
                when (regionIndex) {
                    0 -> constructStackedShortWall(
                        Blackboard.engine,
                        region,
                        stackLowColor,
                        stackTopColor,
                        Point(i - width, j - width)
                    )
                    1 -> constructBillboardTerrain(
                        Blackboard.engine,
                        region,
                        treeColor,
                        Point(i - width, j - width)
                    )
                    else -> constructFloor(
                        Blackboard.engine,
                        region,
                        floorColor,
                        Point(i - width, j - width)
                    )
                }
            }
        }
    }

    // Exiting the tactical layer, clean up...
    fun close() {
        Blackboard.engine.systems.forEach {
            Blackboard.engine.removeSystem(it)
        }
        Blackboard.renderPipeline.clearStages()
    }

    fun update(dt: Float) {
        var proceed = false
        while (!proceed) {
            if (lastState != currentState) {
                println("Proceeding for state $currentState")
            }
            lastState = currentState
            proceed = when (currentState) {
                is TacticalState.ANIMATING_ACTION -> animateAction(dt)
                is TacticalState.ENTITY_ACTING -> getEntityAction()
                is TacticalState.EXECUTING_ACTION -> executeAction()
                is TacticalState.SELECTING_NEXT_ACTION -> selectNextAction()
                is TacticalState.SELECTING_NEXT_ENTITY -> selectNextEntity()
                is TacticalState.WAITING_FOR_PLAYER -> waitForInput()
            }
        }

        Blackboard.engine.update(dt)
    }

    fun render() {
        Blackboard.renderPipeline.render()
    }

    private fun getEntityAction(): Boolean {
        //TODO
        return true
    }

    private fun executeAction(): Boolean {
        val executeState = currentState as TacticalState.EXECUTING_ACTION
        val action = executeState.action

        val event = action.execute()
        processEventAndEnqueueReactions(event)

        currentState = TacticalState.SELECTING_NEXT_ACTION()
        return false
    }

    private fun animateAction(dt: Float): Boolean {
        val animationState = currentState as TacticalState.ANIMATING_ACTION
        val animation = animationState.action.animation()
        val timeTaken = animationState.timeTaken
        val totalDuration = animation.totalDuration()

        val t = timeTaken / totalDuration
        animation.animate(timeTaken)

        val newTimeTaken = timeTaken + dt
        return if (newTimeTaken > totalDuration) {
            // Animation is complete
            animation.cleanup()
            currentState = TacticalState.EXECUTING_ACTION(animationState.action)
            false
        }
        else {
            // Animation must continue, proceed to next frame
            true
        }
    }

    private fun selectNextAction(): Boolean {
        // If no actions remain, try to select an entity
        val nextAction = actionQueue.dequeue()
        if (nextAction == null) {
            // Select an entity instead of proceeding
            currentState = TacticalState.SELECTING_NEXT_ENTITY()
        } else {
            // Attempt the action
            if (tryAction(nextAction)) {
                currentState = TacticalState.ANIMATING_ACTION(nextAction, 0f)
            }
        }
        return false
    }

    private fun selectNextEntity(): Boolean {
        // If no entity available, proceed
        var nextActor = scheduler.getNextActor()
        if (nextActor == null) {
            val fastForward = scheduler.fastForward()
            // No entities queued? player must be next
            if (fastForward == null) {
                currentState = TacticalState.WAITING_FOR_PLAYER()
                return true
            }

            processEventAndEnqueueReactions(TimePassedEvent(fastForward))
            nextActor = scheduler.getNextActor()!!
        }
        currentState = TacticalState.ENTITY_ACTING(nextActor)
        return false
    }

    private fun waitForInput(): Boolean {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            // Rotate CCW
            Blackboard.tacticalCameraDirection = Blackboard.tacticalCameraDirection.ccw()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            // Rotate CW
            Blackboard.tacticalCameraDirection = Blackboard.tacticalCameraDirection.cw()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            // Free/non-free camera toggle
            Blackboard.tacticalCameraDirection = when (Blackboard.tacticalCameraDirection) {
                is TacticalCameraDirection.FREE -> TacticalCameraDirection.NORTH()
                else -> TacticalCameraDirection.FREE(0f)
            }
        }
        return true
    }
}