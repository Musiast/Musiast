package com.example.musiast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity: AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Musiast)
        setContentView(R.layout.activity_dashboard)

        val sharedPref=this?.getPreferences(Context.MODE_PRIVATE)?:return
        val isLogin=sharedPref.getString("Email","1")

        if(isLogin=="1") {
            var email = intent.getStringExtra("email")
            if (email != null) {
                setData(email)
                with(sharedPref.edit())
                {
                    putString("Email", email)
                    apply()
                }
            }
        }
        else {
            setData(isLogin)
        }

        //Experimental: If user is logged in thru google sign in
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        if (user != null) {
            username.text = user.displayName
        }
    }

    private fun setData(email:String?) {
        db= FirebaseFirestore.getInstance()
        if (email != null) {
            db.collection("USERS").document(email).get()
                .addOnSuccessListener {
                        tasks->
                    username.text=tasks.get("Name").toString()
//                  email.text=tasks.get("email").toString()
                }
        }
    }

    fun settings(@Suppress("UNUSED_PARAMETER") view: View?) {
        val intent = Intent(this@DashboardActivity, SettingsActivity::class.java)
        startActivity(intent)
    }
}