package com.github.photos_gallery.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.github.photos_gallery.MainApplication

abstract class BaseActivity : AppCompatActivity() {
 private val app get() = applicationContext as MainApplication
}