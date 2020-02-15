package db.ava.render

import com.badlogic.gdx.graphics.glutils.FrameBuffer

interface RenderStage {
    fun resize(width: Int, height: Int)
    fun getFrameBuffer(): FrameBuffer
}