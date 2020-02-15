package db

import db.ava.game.message.ActionAnimation
import db.ava.game.message.WorldEvent
import db.ava.game.message.event.WorldTime

// Represents an intended change in world state
interface WorldAction {

    // Executed if not blocked by a reactive system
    // Returns the corresponding WorldEvent
    fun execute() : WorldEvent

    // Get the animation associated with this action
    fun animation() : ActionAnimation

    // Get the amount of time that the acting actor is blocked by this action
    fun timeToComplete() : WorldTime
}