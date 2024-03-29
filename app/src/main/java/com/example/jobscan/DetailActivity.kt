package com.example.jobscan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.jobscan.helpers.BottomNavigationHandler
import com.example.jobscan.models.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var bottomNavigationHandler: BottomNavigationHandler
    private lateinit var userId: String
    private lateinit var connectButton: Button
    private lateinit var connectionsRef: DatabaseReference
    private lateinit var currentUserID: String
    private lateinit var databaseRef: DatabaseReference
    private lateinit var usersRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.apply {
            title = "Candidate Detail"
        }
        bottomNavigationView = findViewById(R.id.nav_view) // Correct initialization
        bottomNavigationHandler = BottomNavigationHandler(this)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            bottomNavigationHandler.onNavigationItemSelected(item.itemId)
        }
//        bottomNavigationHandler.selectBottomNavigationItem(bottomNavigationView, R.id.navigation_home)

        val userNameTextView = findViewById<TextView>(R.id.cand_user_name)
        val userImgImageView = findViewById<ImageView>(R.id.cand_profile_image)
        val userTypeTextView = findViewById<TextView>(R.id.cand_user_type)
        val userEmailTextView = findViewById<TextView>(R.id.cand_email)
        val userContactTextView = findViewById<TextView>(R.id.cand_phone_number)
        val userCompanyTextView = findViewById<TextView>(R.id.cand_company)
        val userDesignationTextView = findViewById<TextView>(R.id.cand_designation)
        val userDOBTextView = findViewById<TextView>(R.id.cand_dob)
        val userConnectionCountTextView = findViewById<TextView>(R.id.cand_connection_count)
        val userEducationTextView = findViewById<TextView>(R.id.cand_education_qualification)


        userId = intent.getStringExtra("userId") ?: ""
        currentUserID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        databaseRef = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)
                    userData?.let {
                        // Set user details to views
                        userNameTextView.text = "${it.firstName} ${it.lastName}"
                        userTypeTextView.text = it.userType
                        userEmailTextView.text = it.email
                        userContactTextView.text = it.phoneNumber
                        userCompanyTextView.text = it.companyName
                        userDesignationTextView.text = it.designation
                        userDOBTextView.text = it.dateOfBirth
                        userEducationTextView.text = it.educationQualification
                        userConnectionCountTextView.text =
                            it.connectionCount.toString() + " Connections"
                        Glide.with(this@DetailActivity)
                            .load(it.profileImage) // Replace userImgUrl with the actual URL of the image
                            .placeholder(R.drawable.logo_user) // Optional placeholder image
                            .error(R.drawable.logo_user) // Optional error image
                            .into(userImgImageView)
                        // Load the image using Glide
                        if (it.profileImage.isEmpty()) {
                            Glide.with(this@DetailActivity)
                                .load(R.drawable.logo_user)
                                .placeholder(R.drawable.logo_user) // Optional placeholder image
                                .error(R.drawable.logo_user) // Optional error image
                                .into(userImgImageView)
                        } else {
                            val storageRef: StorageReference =
                                FirebaseStorage.getInstance().getReferenceFromUrl(it.profileImage)
                            Glide.with(this@DetailActivity)
                                .load(storageRef)
                                .placeholder(R.drawable.logo_user) // Optional placeholder image
                                .error(R.drawable.logo_user) // Optional error image
                                .into(userImgImageView)

                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
        connectButton = findViewById(R.id.connectbtn)
        connectionsRef = FirebaseDatabase.getInstance().reference.child("Connections")
        usersRef = FirebaseDatabase.getInstance().reference.child("Users")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)
                    userData?.let {
                        userConnectionCountTextView.text =
                            it.connectionCount.toString() + " Connections"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        checkIfConnected()

        connectButton.setOnClickListener {
            connectUsers()
        }

    }

    private fun checkIfConnected() {
        usersRef.child(currentUserID).child("connections")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists() && snapshot.hasChild(userId)) {
                        // Users are connected, change button color or text as needed
                        connectButton.setBackgroundColor(resources.getColor(R.color.green))
                        connectButton.text = "Connected"
                        connectButton.isEnabled = false // Disable button
                    } else {
                        // Users are not connected
//                    connectButton.setBackgroundColor(resources.getColor(R.color.green))
                        connectButton.text = "Connect"
                        connectButton.isEnabled = true // Enable button
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
    }

    private fun connectUsers() {
        val connectionCurrentMap = HashMap<String, Any>()
        connectionCurrentMap["$userId"] = ""
        val connectionCandidateMap = HashMap<String, Any>()
        connectionCandidateMap["$currentUserID"] = ""

        usersRef.child(currentUserID).child("connections").updateChildren(connectionCurrentMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update connection count for both users
                    usersRef.child(currentUserID).child("connectionCount")
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var count = snapshot.getValue(Int::class.java) ?: 0
                                count++
                                usersRef.child(currentUserID).child("connectionCount")
                                    .setValue(count)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle database error
                            }
                        })

                    // Update UI after successful connection
                    connectButton.setBackgroundColor(resources.getColor(R.color.green))
                    connectButton.text = "Connected"
                    connectButton.isEnabled = false // Disable button
                } else {
                    // Handle connection error
                }
            }
        usersRef.child(userId).child("connections").updateChildren(connectionCandidateMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    usersRef.child(userId).child("connectionCount")
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var count = snapshot.getValue(Int::class.java) ?: 0
                                count++
                                usersRef.child(userId).child("connectionCount").setValue(count)

                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle database error
                            }
                        })
                }
            }
    }

    private fun navigateToCandidateActivity() {
        val candidate = Intent(this, CandidateActivity::class.java)
        startActivity(candidate)
    }
}

