package db.ava.game.message.event

import db.ava.game.message.WorldEvent

// Describes time passing in the world
typealias WorldTime = Int
// 100 WorldTime = 1 diegetic second
const val WORLD_SECOND: WorldTime = 100

data class TimePassedEvent(val time: WorldTime) : WorldEvent