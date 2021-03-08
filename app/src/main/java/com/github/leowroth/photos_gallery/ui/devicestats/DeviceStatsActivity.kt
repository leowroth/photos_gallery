package com.github.leowroth.photos_gallery.ui.devicestats

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.github.leowroth.photos_gallery.R
import com.github.leowroth.photos_gallery.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.device_stats_layout.*

@AndroidEntryPoint
class DeviceStatsActivity : BaseActivity() {
    private lateinit var viewModel: DeviceStatsViewModel

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.device_stats_layout)

        viewModel =
            ViewModelProvider(this).get(DeviceStatsViewModel::class.java)

        val responseXml = resources.openRawResource(R.raw.response)
        viewModel.initDeviceStats(responseXml)

        viewModel.deviceStatsData.observe(this, {
            it?.let {
                chart.data = it
                chart.invalidate()
            }
        })
    }
}