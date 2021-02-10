package com.github.leowroth.photos_gallery.code_challenges

import org.junit.Test
import kotlin.test.assertEquals

fun calculateFx(array: IntArray): Int {
    val subArrays = getAllSubArrays(array, 0, 0, ArrayList())

    return subArrays.map {
        it.reduce { total, element -> total xor element }
    }.reduce { total, element -> total xor element }
}

// see https://www.geeksforgeeks.org/generating-subarrays-using-recursion/
fun getAllSubArrays(
    array: IntArray,
    start: Int,
    end: Int,
    result: ArrayList<ArrayList<Int>>
): ArrayList<ArrayList<Int>> {

    when {
        end == array.size -> return result
        start > end -> getAllSubArrays(array, 0, end + 1, result)
        else -> {
            val subArray = ArrayList<Int>()
            for (i in start until end) {
                subArray.add(array[i])
            }
            subArray.add(array[end])
            result.add(subArray)
            getAllSubArrays(array, start + 1, end, result)

        }
    }
    return result
}

class XorTests {

    @Test
    fun `calculateFX test`() {
        val array = intArrayOf(3, 5, 1)
        assertEquals(2, calculateFx(array))
    }

    @Test
    fun `1getAllSubArrays returns all subArrays`() {
        val array = intArrayOf(3, 5, 1)
        assertEquals(
            arrayListOf(
                arrayListOf(3),
                arrayListOf(3, 5),
                arrayListOf(5),
                arrayListOf(3, 5, 1),
                arrayListOf(5, 1),
                arrayListOf(1)
            ),
            getAllSubArrays(array, 0, 0, ArrayList())
        )
    }
}