package com.motawfik.minigram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Minigram)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}