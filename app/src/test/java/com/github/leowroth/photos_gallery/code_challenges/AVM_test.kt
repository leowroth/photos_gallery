package com.github.leowroth.photos_gallery.code_challenges

import android.graphics.Bitmap

class Queue(val size: Int) {
    val queue = Array<Person>(size)

    fun enqueue(person: Person, position: Int) {
        if (position < 0 || position > queue.size - 1) {
            throw IndexOutOfBoundsException()
        }

        queue[position] = person
    }

    fun dequeue(): Person {
        lateinit var hold: Person
        queue.forEachIndexed { index, person ->
            hold = person
            if (index >= 1) {
                queue[index - 1] = hold
            }
        }
        return hold
    }

    fun isEmpty(): Boolean {
        queue.forEach { if (it == null) return false }
        return true
    }

    data class Person {
        val name: String
    }
}

public class ShareQRFragment : BaseFragment() {
    var x = 0
        private set

    fun getImage(): CodeImage {
        val dimensions = BitMatrix(width, height)
        var bmp =
            Bitmap.createBitmap(
                dimensions.Width,
                dimensions.Height,
                Bitmap.Config.RGB_565
            )

        (0 until width).forEach { x ->
            for (y in 0..height) {
                bmp.setPixel(x, y, bitMatrix.get(height - x, y))
            }
        }

        return codeImage.setImageBitmap(bmp)
    }
}