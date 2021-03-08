package com.github.leowroth.photos_gallery.ui.devicestats

import android.graphics.Color.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.leowroth.photos_gallery.ui.base.BaseViewModel
import com.github.leowroth.photos_gallery.utils.AvmXmlParser
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

class DeviceStatsViewModel : BaseViewModel() {

    private var deviceStats = MutableLiveData(LineData())
    val deviceStatsData: LiveData<LineData> get() = deviceStats

    fun initDeviceStats(responseXml: InputStream) {
        viewModelScope.launch(Dispatchers.IO) {
            val parsedDeviceStats = AvmXmlParser().parse(responseXml)

            val temperatureLineDataSet =
                LineDataSet(
                    parsedDeviceStats.getTemperatureEntries(),
                    "Temperature"
                )
            temperatureLineDataSet.color = RED
            temperatureLineDataSet.setCircleColor(RED)

            val voltageLineDataSet =
                LineDataSet(parsedDeviceStats.getVoltageEntries(), "Voltage")
            voltageLineDataSet.color = GREEN
            voltageLineDataSet.setCircleColor(GREEN)

            val powerLineDataSet =
                LineDataSet(parsedDeviceStats.getPowerEntries(), "Power")
            powerLineDataSet.color = BLUE
            powerLineDataSet.setCircleColor(BLUE)

            val energyLineDataSet =
                LineDataSet(parsedDeviceStats.getEnergyEntries(), "Energy")
            energyLineDataSet.color = YELLOW
            energyLineDataSet.setCircleColor(YELLOW)

            val dataSets = arrayListOf<ILineDataSet>()
            dataSets.add(temperatureLineDataSet)
            dataSets.add(voltageLineDataSet)
            dataSets.add(powerLineDataSet)
            dataSets.add(energyLineDataSet)

            deviceStats.postValue(LineData(dataSets))
        }
    }
}