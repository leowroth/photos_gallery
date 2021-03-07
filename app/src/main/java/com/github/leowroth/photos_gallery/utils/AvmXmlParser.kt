package com.github.leowroth.photos_gallery.utils

import android.util.Xml
import com.github.leowroth.photos_gallery.utils.AvmXmlParser.DeviceStats.Companion.COUNT
import com.github.leowroth.photos_gallery.utils.AvmXmlParser.DeviceStats.Companion.ENERGY
import com.github.leowroth.photos_gallery.utils.AvmXmlParser.DeviceStats.Companion.GRID
import com.github.leowroth.photos_gallery.utils.AvmXmlParser.DeviceStats.Companion.POWER
import com.github.leowroth.photos_gallery.utils.AvmXmlParser.DeviceStats.Companion.STATS
import com.github.leowroth.photos_gallery.utils.AvmXmlParser.DeviceStats.Companion.TEMPERATURE
import com.github.leowroth.photos_gallery.utils.AvmXmlParser.DeviceStats.Companion.VOLTAGE
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.util.*

// We don't use namespaces
private val ns: String? = null

class AvmXmlParser {

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): DeviceStats {
        inputStream.use {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            return readDeviceStats(parser)
        }
    }

    data class DeviceStats(
        val temperature: List<Stats>?,
        val voltage: List<Stats>?,
        val power: List<Stats>?,
        val energy: List<Stats>?
    ) {
        companion object {
            const val DEVICESTATS = "devicestats"
            const val TEMPERATURE = "temperature"
            const val VOLTAGE = "voltage"
            const val POWER = "power"
            const val ENERGY = "energy"
            const val STATS = "stats"
            const val COUNT = "count"
            const val GRID = "grid"
        }

    }

    data class Stats(
        val count: Int,
        val grid: Int,
        val values: Array<String>
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Stats

            if (count != other.count) return false
            if (grid != other.grid) return false
            if (!values.contentEquals(other.values)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = count
            result = 31 * result + grid
            result = 31 * result + values.contentHashCode()
            return result
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readDeviceStats(parser: XmlPullParser): DeviceStats {
        parser.require(XmlPullParser.START_TAG, ns, DeviceStats.DEVICESTATS)
        var temperature: List<Stats>? = null
        var voltage: List<Stats>? = null
        var power: List<Stats>? = null
        var energy: List<Stats>? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                TEMPERATURE -> temperature = readTemperature(parser)
                VOLTAGE -> voltage = readVoltage(parser)
                POWER -> power = readPower(parser)
                ENERGY -> energy = readEnergy(parser)
                else -> skip(parser)
            }
        }

        return DeviceStats(temperature, voltage, power, energy)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readPower(parser: XmlPullParser): List<Stats> {
        parser.require(XmlPullParser.START_TAG, ns, POWER)
        val energy = readStats(parser)
        parser.require(XmlPullParser.END_TAG, ns, POWER)
        return energy
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readVoltage(parser: XmlPullParser): List<Stats> {
        parser.require(XmlPullParser.START_TAG, ns, VOLTAGE)
        val energy = readStats(parser)
        parser.require(XmlPullParser.END_TAG, ns, VOLTAGE)
        return energy
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readEnergy(parser: XmlPullParser): List<Stats> {
        parser.require(XmlPullParser.START_TAG, ns, ENERGY)
        val energy = readStats(parser)
        parser.require(XmlPullParser.END_TAG, ns, ENERGY)
        return energy
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTemperature(parser: XmlPullParser): List<Stats> {
        parser.require(XmlPullParser.START_TAG, ns, TEMPERATURE)
        val temperature = readStats(parser)
        parser.require(XmlPullParser.END_TAG, ns, TEMPERATURE)
        return temperature
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readStats(parser: XmlPullParser): List<Stats> {
        val statsList = ArrayList<Stats>()
        while (parser.nextTag() == XmlPullParser.START_TAG) {
            parser.require(XmlPullParser.START_TAG, ns, STATS)
            val count = parser.getAttributeValue(null, COUNT)
            val grid = parser.getAttributeValue(null, GRID)
            val values = readText(parser)
            if (count == null || grid == null) {
                throw IOException("Couldn't parse count or grid in stats")
            }
            parser.require(XmlPullParser.END_TAG, ns, STATS)
            statsList.add(Stats(count.toInt(), grid.toInt(), values))
        }
        return statsList
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readText(parser: XmlPullParser): Array<String> {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result.trimIndent().split(",").toTypedArray()
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}