package com.example.jobscan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

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
                        val userEmail = snapshot.child("email").getValue(String::class.java)
                        val userContact = snapshot.child("phoneNumber").getValue(String::class.java)
                        val userImg = snapshot.child("profileImage").getValue(String::class.java)
                        val userCompany = snapshot.child("companyName").getValue(String::class.java)
                        val educationQualification =
                            snapshot.child("educationQualification").getValue(String::class.java)
                        val designation = snapshot.child("designation").getValue(String::class.java)
                        val userType = snapshot.child("userType").getValue(String::class.java)
                        val connectionCount =
                            snapshot.child("connectionCount").getValue(Int::class.java)
                        val dob = snapshot.child("dateOfBirth").getValue(String::class.java)
//                        findViewById<TextView>(R.id.userName).text = "$firstName $lastName"
//                        findViewById<TextView>(R.id.userEmail).text = userEmail
//                        findViewById<TextView>(R.id.userContact).text = userContact
                        findViewById<TextView>(R.id.pr_user_name).text = "$firstName $lastName"
                        findViewById<TextView>(R.id.pr_email).text = userEmail
                        findViewById<TextView>(R.id.cand_phone_number).text = userContact
                        findViewById<TextView>(R.id.pr_designation).text = designation
                        findViewById<TextView>(R.id.pr_company).text = userCompany
                        findViewById<TextView>(R.id.cand_connection_count).text =
                            connectionCount.toString() + " Connections"
                        findViewById<TextView>(R.id.pr_dob).text = dob
                        findViewById<TextView>(R.id.pr_education_qualification).text =
                            educationQualification
                        findViewById<TextView>(R.id.pr_user_type).text = userType
                        if (!userImg.isNullOrEmpty()) {
                            Log.i("test", "UserImage value $userImg")
                            val storageReference: StorageReference =
                                FirebaseStorage.getInstance().getReferenceFromUrl(userImg)
                            Glide.with(this@ProfileActivity)
                                .load(storageReference)
                                .into(findViewById(R.id.pr_profile_image))
                        }
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
        }
}


