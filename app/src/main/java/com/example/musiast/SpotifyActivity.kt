package com.example.musiast

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class SpotifyActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Musiast)
        setContentView(R.layout.activity_spotify)

        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.spotify

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard -> {
                    val intent = Intent(this@SpotifyActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.insights -> {
                    val intent = Intent(this@SpotifyActivity, HomeActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.spotify -> return@OnNavigationItemSelectedListener true
            }
            false
        })
    }

    fun search(view: View) {
        val queryInputView = findViewById<EditText>(R.id.queryInput)
        val query = queryInputView.text.toString()

        lifecycleScope.launch {
            val lyrics = LyricsFinder.find(query)
            findViewById<TextView>(R.id.lyricsView).text = lyrics
        }
    }


}