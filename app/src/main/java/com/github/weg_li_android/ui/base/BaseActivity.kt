package com.github.weg_li_android.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.github.weg_li_android.MainApplication

abstract class BaseActivity : AppCompatActivity() {
 private val app get() = applicationContext as MainApplication
}