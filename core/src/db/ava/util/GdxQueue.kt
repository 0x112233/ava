package db.ava.util

import ktx.collections.GdxArray
import kotlin.math.max

const val STARTING_CAPACITY = 16

// TODO: test this!
// Queue implementation backed by GdxArray as circular buffer
class GdxQueue<T>(capacity: Int = STARTING_CAPACITY) {
    private val data = GdxArray<T>(capacity)

    private var start = 0
    private var end = 1

    fun enqueue(value: T) {
        if (start == end) {
            // We must resize!
            val newCapacity = max(data.size * 2, STARTING_CAPACITY)
            expand(newCapacity)
        }

        data[end] = value
        end++
        end %= capacity()
    }

    fun dequeue() : T? {
        return if (size() == 0) {
            null
        } else {
            val out = data[start]!!
            data[start] = null
            start++
            start %= capacity()
            out
        }
    }

    fun clear() {
        data.clear()
        start = 0
        end = 1
    }

    fun size() : Int {
        return when{
            start < end -> end - start - 1
            start > end -> end + capacity() - start
            else -> capacity()
        }
    }

    private inline fun capacity() : Int {
        return data.size
    }

    private fun expand(newCapacity: Int) {

    }
}