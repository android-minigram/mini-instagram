package com.motawfik.minigram

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.motawfik.minigram.data.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth()
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Minigram)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                firebaseAuth.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}