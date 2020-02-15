package db.ava.ai

import db.WorldAction

interface Actor {
    fun getNextAction() : WorldAction
}