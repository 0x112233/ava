package db.ava.world

import com.badlogic.ashley.core.Entity
import ktx.collections.GdxArray

// Map world coordinates to entities
interface SpatialLookup {
    // Insert the entity at the given coordinates
    // Return true if the entity was successfully inserted
    fun put(point: Point, entity: Entity) : Boolean
    // Get all entities, or null if none, at the given coordinates
    fun get(point: Point) : GdxArray<Entity>?
    // Delete entity from given coordinates
    // Return true if it's successfully deleted
    fun delete(entity: Entity) : Boolean
    // Find entity's coordinates, or null if it isn't present
    fun getPosition(entity: Entity) : Point?
}