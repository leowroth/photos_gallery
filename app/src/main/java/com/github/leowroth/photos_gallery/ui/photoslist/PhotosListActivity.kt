package com.github.leowroth.photos_gallery.ui.photoslist

import android.app.AlertDialog
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.github.leowroth.photos_gallery.GlideApp
import com.github.leowroth.photos_gallery.GlideRequest
import com.github.leowroth.photos_gallery.GlideRequests
import com.github.leowroth.photos_gallery.R
import com.github.leowroth.photos_gallery.domain.model.Photo
import com.github.leowroth.photos_gallery.ui.base.BaseActivity
import com.github.leowroth.photos_gallery.utils.AvmXmlParser
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.line_chart_dialog.view.*
import kotlinx.android.synthetic.main.photos_list.*

@AndroidEntryPoint
class PhotosListActivity : BaseActivity() {
    private val amountToPreload = 10
    private lateinit var viewModel: PhotosListViewModel
    private lateinit var internetErrorSnackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photos_list)

        internetErrorSnackbar = setupSnackbar()

        viewModel = ViewModelProvider(this).get(PhotosListViewModel::class.java)

        viewModel.eventLoadingData.observe(this, {
            if (it) progressCircular.visibility = View.VISIBLE
            else {
                progressCircular.visibility = View.INVISIBLE
            }
        })

        val responseXml = resources.openRawResource(R.raw.response)
        val deviceStats = AvmXmlParser().parse(responseXml)
        showLineChartDialog(deviceStats)

        viewModel.eventNetworkErrorData.observe(this, {
            if (it) internetErrorSnackbar.show()
        })

        viewModel.initDataFromRepository()

        viewModel.photosList.observe(this, { photosList ->
            val glideRequest = GlideApp.with(applicationContext)
            val fullRequest =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    glideRequest.asDrawable().fitCenter()
                        .placeholder(ColorDrawable(getColor(R.color.primaryColor)))
                        .error(R.mipmap.error_cat)
                } else {
                    glideRequest.asDrawable().fitCenter()
                        .placeholder(ColorDrawable(Color.MAGENTA))
                        .error(R.mipmap.error_cat)
                }

            val sizeProvider = ViewPreloadSizeProvider<Photo>()

            val adapter = setupAdapter(photosList, sizeProvider, fullRequest)
            setupRecyclerView(adapter, glideRequest, sizeProvider)
        })

    }

    private fun showLineChartDialog(deviceStats: AvmXmlParser.DeviceStats) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.devicestats))

        val layout = layoutInflater.inflate(R.layout.line_chart_dialog, null)
        builder.setView(layout)
        val dialog = builder.create()

        val temperatureLineDataSet =
            LineDataSet(deviceStats.getTemperatureEntries(), "Temperature")
        temperatureLineDataSet.color = Color.RED

        val voltageLineDataSet =
            LineDataSet(deviceStats.getVoltageEntries(), "Voltage")
        voltageLineDataSet.color = Color.GREEN

        val powerLineDataSet =
            LineDataSet(deviceStats.getPowerEntries(), "Power")
        powerLineDataSet.color = Color.BLUE

        val energyLineDataSet =
            LineDataSet(deviceStats.getEnergyEntries(), "Energy")
        energyLineDataSet.color = Color.YELLOW

        val dataSets = arrayListOf<ILineDataSet>()
        dataSets.add(temperatureLineDataSet)
        dataSets.add(voltageLineDataSet)
        dataSets.add(powerLineDataSet)
        dataSets.add(energyLineDataSet)

        val data = LineData(dataSets)
        layout.chart.data = data
        layout.chart.invalidate()

        dialog.show()
    }

    private fun setupSnackbar(): Snackbar {
        return Snackbar.make(
            photosRecyclerView,
            getString(R.string.network_error),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.try_again)) {
                viewModel.forceRefreshDataFromRepository()
                internetErrorSnackbar.dismiss()
            }
    }

    private fun setupAdapter(
        photosList: List<Photo>,
        sizeProvider: ViewPreloadSizeProvider<Photo>,
        fullRequest: GlideRequest<Drawable>
    ): PhotosListAdapter {
        val adapter = PhotosListAdapter(photosList, sizeProvider, fullRequest)
        adapter.setHasStableIds(true)
        adapter.onItemClick = { position ->
            viewModel.onPhotoClicked(position)
        }
        return adapter
    }

    private fun setupRecyclerView(
        adapter: PhotosListAdapter,
        glideRequest: GlideRequests,
        sizeProvider: ViewPreloadSizeProvider<Photo>
    ) {
        photosRecyclerView.swapAdapter(adapter, false)

        val preLoader = RecyclerViewPreloader(
            glideRequest, adapter, sizeProvider, amountToPreload
        )
        photosRecyclerView.addOnScrollListener(preLoader)
        photosRecyclerView.setItemViewCacheSize(0)
        photosRecyclerView.setRecyclerListener { holder ->
            glideRequest.clear(holder.itemView)
        }

        val lm = photosRecyclerView.layoutManager as GridLayoutManager
        if (resources.configuration.orientation == ORIENTATION_PORTRAIT) {
            lm.spanCount = 1
        } else {
            lm.spanCount = 2
        }
    }
}