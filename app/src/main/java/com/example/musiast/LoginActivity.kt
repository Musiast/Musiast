package com.example.musiast

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.common.base.Ascii.toLowerCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Musiast)
        setContentView(R.layout.activity_login)
        auth= FirebaseAuth.getInstance()

        button2.setOnClickListener {
            if(checking()){
                val email= toLowerCase(editTextTextEmailAddress.text.toString())
                val password= editTextTextPassword.text.toString()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this,DashboardActivity::class.java)
                            intent.putExtra("email",email)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Incorrect Email or Password!", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            else{
                Toast.makeText(this,"Enter the Details!",Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun checking():Boolean
    {
        if(editTextTextEmailAddress.text.toString().trim{it<=' '}.isNotEmpty()
            && editTextTextPassword.text.toString().trim{it<=' '}.isNotEmpty())
        {
            return true
        }
        return false
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}