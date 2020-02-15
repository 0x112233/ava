package db.ava.util

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import ktx.collections.GdxMap

object ComponentLookup {
    val data = GdxMap<Class<*>, ComponentMapper<*>>()

     inline fun<reified T: Component> register() {
        val clazz = T::class.java
         if (!data.containsKey(clazz)) {
             data.put(clazz, ComponentMapper.getFor(clazz))
         }
    }

    inline fun<reified T: Component> has(entity: Entity) : Boolean {
       return (data[T::class.java] as ComponentMapper<T>).has(entity)
    }

    inline fun<reified T: Component> get(entity: Entity) : T? {
        return (data[T::class.java] as ComponentMapper<T>).get(entity)
    }
}