package db.ava.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.decals.Decal
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Array
import db.ava.util.Blackboard

class TacticalDecalGroupStrategy : GroupStrategy {
    lateinit var shaderProgram: ShaderProgram

    override fun afterGroup(group: Int) {}

    override fun beforeGroups() {
        //TODO: move to asset manager
        if (!::shaderProgram.isInitialized) {
            shaderProgram = ShaderProgram(
                Gdx.files.internal("assets/default.vert"),
                Gdx.files.internal("assets/default.frag")
            )
        }

        Blackboard.tacticalCamera.compose(Blackboard.tacticalCameraDirection)
        shaderProgram.begin()
        shaderProgram.setUniformMatrix("u_projTrans", Blackboard.tacticalCamera.combined())
        //shaderProgram.setUniformi("u_texture", 0)
    }

    override fun afterGroups() {
        shaderProgram.end()
    }

    override fun beforeGroup(group: Int, contents: Array<Decal>?) {}

    override fun getGroupShader(group: Int): ShaderProgram {
        return shaderProgram!!
    }

    override fun decideGroup(decal: Decal?): Int {
        return 0 // TODO
    }
}