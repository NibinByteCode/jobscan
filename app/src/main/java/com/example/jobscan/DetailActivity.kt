package com.example.jobscan
import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.jobscan.helpers.BottomNavigationHandler
import com.example.jobscan.models.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var bottomNavigationHandler: BottomNavigationHandler
    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var databaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.apply {
            title = "Profile"
        }
        bottomNavigationView = findViewById(R.id.nav_view) // Correct initialization
        bottomNavigationHandler = BottomNavigationHandler(this)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            bottomNavigationHandler.onNavigationItemSelected(item.itemId)
        }
//        bottomNavigationHandler.selectBottomNavigationItem(bottomNavigationView, R.id.navigation_home)

        val userNameTextView = findViewById<TextView>(R.id.userName)
        val userIdTextView = findViewById<TextView>(R.id.userId)
        val userTypeTextView = findViewById<TextView>(R.id.userType)
        val userEmailTextView = findViewById<TextView>(R.id.userEmail)
        val userContactTextView = findViewById<TextView>(R.id.userContact)
        val userCompanyTextView = findViewById<TextView>(R.id.userCompany)
        val userDesignationTextView = findViewById<TextView>(R.id.userDesignation)


        userId = intent.getStringExtra("userId") ?: ""
        databaseRef = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)
                    userData?.let {
                        // Set user details to views
                        userNameTextView.text = "${it.firstName} ${it.lastName}"
                        userIdTextView.text = it.userId
                        userTypeTextView.text = it.userType
                        userEmailTextView.text = it.email
                        userContactTextView.text = it.phoneNumber
                        userCompanyTextView.text = it.companyName
                        userDesignationTextView.text = it.designation
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })

    }

    private fun navigateToCandidateActivity() {
        val candidate = Intent(this, CandidateActivity::class.java)
        startActivity(candidate)
    }
}

