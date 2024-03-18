package com.example.jobscan

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.jobscan.databinding.ActivitySignUpBinding
import androidx.appcompat.app.AppCompatActivity
import com.example.jobscan.models.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Sign Up"
        }
        val text = findViewById<TextView>(R.id.textView2)
        firebaseAuth = FirebaseAuth.getInstance()
        text.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            Log.d("msg", "Clicked")
            val email = binding.userEmail.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastname.text.toString()
            val dateOfBirth = binding.dateOfBirth?.text.toString()
            val companyName = binding.companyName?.text.toString()
            val designation = binding.designation?.text.toString()
            val educationQualification = binding.qualification?.text.toString()
            val phoneNumber = binding.phoneNumber.text.toString()
            val userType = when (binding.userTypeRadioGroup?.checkedRadioButtonId) {
                R.id.userTypeUser -> "Candidate"
                R.id.userTypeAdmin -> "Recruiter"
                else -> ""
            }
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty() && phoneNumber.isNotEmpty()) {
                if (!android.util.Patterns.PHONE.matcher(phoneNumber).matches()) {
                    // Phone number is not valid
                    Toast.makeText(this, "Phone number should have 10 digit", Toast.LENGTH_SHORT).show()
                }
                else if(!validEmail(email)){
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()

                }
                else if (!isValidDate(dateOfBirth)) {
                    // Date of birth is not valid
                    Toast.makeText(this, "Date Must Be in DD/MM/YYYY Format", Toast.LENGTH_SHORT).show()
                }
                else if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val userId = firebaseAuth.currentUser?.uid

                                FirebaseDatabase.getInstance().getReference("Users").child(userId!!)
                                    .setValue(UserData(firstName=firstName,lastName=lastName,email=email,phoneNumber=phoneNumber,userType=userType,dateOfBirth=dateOfBirth, companyName = companyName, designation = designation, educationQualification = educationQualification,userId=userId))
                                    .addOnSuccessListener {

                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to register: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(
                                    this,
                                    it.exception.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
            }

        }
    }private fun validEmail(email: String): Boolean {
        val emailPatterns = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailPatterns.toRegex())
    }
    private fun isValidDate(date: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
        dateFormat.isLenient = false
        return try {
            dateFormat.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }
}
