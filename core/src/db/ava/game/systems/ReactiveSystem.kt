package db.ava.game.systems

import db.WorldAction
import db.ava.game.message.WorldEvent
import ktx.collections.GdxArray

interface ReactiveSystem {

    // Process an attempted action
    // Return true if allowing the action, false otherwise
    fun allowAction(action: WorldAction) : Boolean

    // Process an event
    fun onEvent(event: WorldEvent) : GdxArray<WorldAction>?
}