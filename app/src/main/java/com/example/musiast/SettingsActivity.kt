package com.example.musiast

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsActivity:AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Musiast)
        setContentView(R.layout.activity_settings)
        mAuth = FirebaseAuth.getInstance()
    }

    fun logout(@Suppress("UNUSED_PARAMETER") view: View?){
        Firebase.auth.signOut()
        mAuth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun settings_back(@Suppress("UNUSED_PARAMETER") view: View?) {
        val intent = Intent(this@SettingsActivity, DashboardActivity::class.java)
        startActivity(intent)
    }
}