package db.ava.render

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.decals.Decal
import ktx.collections.GdxArray

sealed class TacticalRenderable {
    data class Billboard(val decal: Decal = Decal()) : TacticalRenderable()

    data class CrossBillboard(
        val region: TextureRegion = TextureRegion(),
        var count: Int = 2,
        val decals: GdxArray<Decal> = GdxArray()
    ) : TacticalRenderable()

    data class Tile(val decal: Decal = Decal()) : TacticalRenderable()

    data class TileStack(val decals: GdxArray<Decal> = GdxArray()) : TacticalRenderable()
}