package com.example.musiast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SignUpActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Musiast)
        setContentView(R.layout.activity_signup)
    }
}