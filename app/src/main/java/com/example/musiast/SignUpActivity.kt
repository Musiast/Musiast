package com.example.musiast

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.common.base.Ascii.toLowerCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity: AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Musiast)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        db= FirebaseFirestore.getInstance()

        SignUp.setOnClickListener{
            if(checking())
            {
                var email = toLowerCase(login_emailid.text.toString())
                var password = login_password.text.toString()
                var name = login_name.text.toString()
                val user = hashMapOf(
                    "Name" to name,
                    "email" to email
                )
                val Users = db.collection("USERS")
                val query = Users.whereEqualTo("email",email).get()
                    .addOnSuccessListener {
                            tasks->
                        if(tasks.isEmpty)
                        {
                            auth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(this){
                                        task->
                                    if(task.isSuccessful)
                                    {
                                        Users.document(email).set(user)
                                        val intent= Intent(this,DashboardActivity::class.java)
                                        intent.putExtra("email",email)
                                        startActivity(intent)
                                        finish()
                                    }
                                    else
                                    {
                                        Toast.makeText(this,"Enter a valid Email and Password!", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                        else
                        {
                            Toast.makeText(this,"User Already Registered!", Toast.LENGTH_LONG).show()
                            val intent= Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
            }
            else{
                Toast.makeText(this,"Enter the Details!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checking():Boolean{
        if(login_name.text.toString().trim {it<=' '}.isNotEmpty()
            && login_emailid.text.toString().trim {it<=' '}.isNotEmpty()
            && login_password.text.toString().trim {it<=' '}.isNotEmpty()
        )
        {
            return true
        }
        return false
    }

    fun backBtn(@Suppress("UNUSED_PARAMETER")view: View?) {
        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
        startActivity(intent)
    }
}