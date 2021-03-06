package com.github.leowroth.photos_gallery.utils

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

// We don't use namespaces
private val ns: String? = null

class AvmXmlParser {

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List<*> {
        inputStream.use {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    private fun readFeed(parser: XmlPullParser): List<DeviceStats> {
        val deviceStats = mutableListOf<DeviceStats>()

        parser.require(XmlPullParser.START_TAG, ns, "devicestats")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the entry tag
            if (parser.name == "devicestats") {
                deviceStats.add(readDeviceStats(parser))
            } else {
                skip(parser)
            }
        }
        return deviceStats

    }

    data class DeviceStats(
        val temperature: List<Stats>?,
        val voltage: List<Stats>?,
        val power: List<Stats>?,
        val energy: List<Stats>?
    )

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
        parser.require(XmlPullParser.START_TAG, ns, "devicestats")
        var temperature: List<Stats>? = null
        var voltage: List<Stats>? = null
        var power: List<Stats>? = null
        var energy: List<Stats>? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "temperature" -> temperature = readTemperature(parser)
//                "voltage" -> voltage = readVoltage(parser)
//                "power" -> power = readPower(parser)
//                "energy" -> energy = readEnergy(parser)
                else -> skip(parser)
            }
        }

        return DeviceStats(temperature, voltage, power, energy)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTemperature(parser: XmlPullParser): List<Stats>? {
        parser.require(XmlPullParser.START_TAG, ns, "temperature")
        val temperature = readStats(parser)
        parser.require(XmlPullParser.END_TAG, ns, "temperature")
        return temperature
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readStats(parser: XmlPullParser): List<Stats>? {
        parser.require(XmlPullParser.START_TAG, ns, "stats")
        val values = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "stats")
        return arrayListOf(Stats(2, 3, values))
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readText(parser: XmlPullParser): Array<String> {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result.split(",").toTypedArray()
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