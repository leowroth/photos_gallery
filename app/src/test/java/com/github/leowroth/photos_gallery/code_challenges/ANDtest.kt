package com.github.leowroth.photos_gallery.code_challenges

import org.junit.Test
import kotlin.test.assertEquals

fun andCompare(a: Array<Int>, b: Array<Int>): Int {
//b should always be > than a
    if (a.size > b.size) throw NumberFormatException()
    //convert to int for easier calculations
    val aInt = convertBitArrayToInt(a)
    val bInt = convertBitArrayToInt(b)

    var result = aInt

    //do the actual magic of calculating the and for every
    // incremental int between a and b
    for (i in aInt..bInt) {
        result = result and i
    }

    return result
}

fun convertBitArrayToInt(bitArray: Array<Int>): Int {
    var multiplier = 1
    var result = 0
    for (element in bitArray) {
        result += (multiplier * element)
        multiplier *= 2
    }
    return result
}

class Tests {

    @Test
    fun `andCompare returns and of two arrays`() {
        val a = arrayOf(1, 0, 1)
        val b = arrayOf(1, 1, 1)

        val actual = andCompare(a, b)

        assertEquals(4, actual)
    }

    @Test
    fun `convertBitArrayToInt test`() {
        val a = arrayOf(1, 1, 1)
        val b = arrayOf(1, 0, 1)

        assertEquals(7, convertBitArrayToInt(a))
        assertEquals(5, convertBitArrayToInt(b))
    }
}