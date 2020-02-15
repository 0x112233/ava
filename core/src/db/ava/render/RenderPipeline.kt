package db.ava.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.collections.GdxArray

class RenderPipeline {
    val stages = GdxArray<RenderStage>()
    val nullCamera = OrthographicCamera()
    var width = 0
    var height = 0

    lateinit var spriteBatch: SpriteBatch

    fun init() {
        spriteBatch = SpriteBatch()
    }

    fun addStage(stage: RenderStage) {
        stages.add(stage)
    }

    fun clearStages() {
        stages.clear()
    }

    fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height

        nullCamera.setToOrtho(true, width.toFloat(), height.toFloat())
        nullCamera.position.set(width.toFloat() / 2, height.toFloat() / 2, 0f)
        nullCamera.update()

        stages.forEach { it.resize(width, height) }
    }

    fun render() {
        Gdx.gl.glClearColor(0f, 1f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        spriteBatch.projectionMatrix = nullCamera.combined
        spriteBatch.begin()
        stages.forEach {
            val tex = it.getFrameBuffer().colorBufferTexture
            spriteBatch.draw(tex, 0f, 0f, width.toFloat(), height.toFloat())
        }
        spriteBatch.end()
    }
}