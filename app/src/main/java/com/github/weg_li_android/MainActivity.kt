package com.github.weg_li_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.github.weg_li_android.ui.base.ViewModelFactory
import com.github.weg_li_android.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()

        sendButton.setOnClickListener {
            startEmailIntent()
        }

//        mainViewModel.districts.observe(this, androidx.lifecycle.Observer { list ->
//            // TODO do something with districts
//        })
    }

    private fun startEmailIntent() {
//        val intent = Intent(Intent.ACTION_SEND).apply {
//            type = "*/*"
//            //TODO change to correct address
//            putExtra(Intent.EXTRA_EMAIL, "name@email.de")
//            putExtra(Intent.EXTRA_SUBJECT, mainViewModel.getReport().getEmail())
//            //TODO Add photos
//            //putExtra(Intent.EXTRA_STREAM, attachment)
//        }
//        if (intent.resolveActivity(packageManager) != null) {
//            startActivity(intent)
//        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory()
        ).get(MainViewModel::class.java)
    }
}