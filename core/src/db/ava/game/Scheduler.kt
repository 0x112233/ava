package db.ava.game

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import db.ava.ai.Actor
import db.ava.game.message.event.WorldTime
import ktx.collections.GdxArray
import ktx.collections.GdxList

class Scheduler {

    private data class Entry(var time: WorldTime, val entity: Entity)

    private val actors = GdxArray<Entry>()

    // Return the next ready actor
    // Return null if none are ready
    fun getNextActor() : Entity? {
        //TODO: implement
        return null
    }

    // Move time forward until at least one actor is available or the bound is reached
    // And return the amount of time fastforwarded
    // Returns null if no actors available
    fun fastForward() : WorldTime? {
        // TODO: implement
        return null
    }

    fun insert(delay: WorldTime, actor: Actor) {}
}