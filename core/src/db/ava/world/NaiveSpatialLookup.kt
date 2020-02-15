package db.ava.world

import com.badlogic.ashley.core.Entity
import ktx.collections.GdxArray
import ktx.collections.GdxMap

class NaiveSpatialLookup : SpatialLookup {
    val entityLookup = GdxMap<Point, GdxArray<Entity>>()
    val pointLookup = GdxMap<Entity, Point>()

    override fun put(point: Point, entity: Entity) : Boolean {
        if (pointLookup.containsKey(entity)) {
            return false
        }
        if (!entityLookup.containsKey(point)) {
            entityLookup.put(point, GdxArray(8))
        }
        entityLookup[point].add(entity)
        return true
    }

    override fun get(point: Point) : GdxArray<Entity>? {
        return entityLookup.get(point)
    }

    override fun delete(entity: Entity) : Boolean  {
        if (!pointLookup.containsKey(entity)) {
            return false
        }
        val point = pointLookup[entity]
        entityLookup[point].removeValue(entity, true)
        pointLookup.remove(entity)
        return true;
    }

    override fun getPosition(entity: Entity) : Point? {
        return pointLookup[entity]
    }
}