package com.example.jobscan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.apply {
            title = "Profile"
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")

        val currentUser: FirebaseUser? = auth.currentUser
        currentUser?.uid?.let { userId ->
//            val tempUserid = "0cPYokz5OAdEWGcGgrb2oikLtvp1"
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val firstName = snapshot.child("firstName").getValue(String::class.java)
                        val lastName = snapshot.child("lastName").getValue(String::class.java)
                        val userEmail = snapshot.child("userEmail").getValue(String::class.java)
                        val userContact = snapshot.child("userContact").getValue(String::class.java)
                        val userImg = snapshot.child("userImg").getValue(String::class.java)
                        val userCompany = snapshot.child("userCompany").getValue(String::class.java)


                        findViewById<TextView>(R.id.userName).text = "$firstName $lastName"
                        findViewById<TextView>(R.id.userEmail).text = userEmail
                        findViewById<TextView>(R.id.userContact).text = userContact
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle cancelled event
                }
            })
        }


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val logoutBtn: Button = findViewById(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })


        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this@ProfileActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    })
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_connections -> {
                    startActivity(
                        Intent(
                            this@ProfileActivity,
                            CandidateActivity::class.java
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_profile -> {
                    startActivity(Intent(this@ProfileActivity, ProfileActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }}


