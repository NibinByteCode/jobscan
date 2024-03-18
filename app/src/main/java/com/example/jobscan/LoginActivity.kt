package com.example.jobscan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.example.jobscan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inflating the layout
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        firebaseAuth= FirebaseAuth.getInstance()
        val text = findViewById<TextView>(R.id.textView2)
        //intending to login page when the new user text is clicked
        text.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        //Doing login when login button is clicked
        binding.button2.setOnClickListener {
            val email = binding.userEmail.text.toString()
            val password = binding.password.text.toString()
            //empty field validation
            if (email.isNotEmpty() && password.isNotEmpty()) {
                //login using firebase email and password authentication
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

    }
}