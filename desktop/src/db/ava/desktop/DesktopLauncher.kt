package db.ava.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import db.ava.MainGame

fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration()

    config.width = 1920
    config.height = 1080

    LwjglApplication(MainGame(), config)
}