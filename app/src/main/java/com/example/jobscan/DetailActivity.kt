package com.example.jobscan
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.jobscan.helpers.BottomNavigationHandler
import com.example.jobscan.models.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        databaseRef = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)
                    userData?.let {
                        // Set user details to views
                        userNameTextView.text = "${it.firstName} ${it.lastName}"
//                        userIdTextView.text = it.userId
                        userTypeTextView.text = it.userType
                        userEmailTextView.text = it.email
                        userContactTextView.text = it.phoneNumber
                        userCompanyTextView.text = it.companyName
                        userDesignationTextView.text = it.designation
                        userDOBTextView.text = it.dateOfBirth
                        userEducationTextView.text = it.educationQualification
                        userConnectionCountTextView.text = it.connectionCount.toString() + " Connections"
                        Glide.with(this@DetailActivity)
                            .load(it.profileImage) // Replace userImgUrl with the actual URL of the image
                            .placeholder(R.drawable.logo_user) // Optional placeholder image
                            .error(R.drawable.logo_user) // Optional error image
                            .into(userImgImageView)
                        // Load the image using Glide
                        if(it.profileImage.isEmpty()){
                            Glide.with(this@DetailActivity)
                                .load(R.drawable.logo_user)
                                .placeholder(R.drawable.logo_user) // Optional placeholder image
                                .error(R.drawable.logo_user) // Optional error image
                                .into(userImgImageView)
                        }else {
                            val storageRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(it.profileImage)
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

    }

    private fun navigateToCandidateActivity() {
        val candidate = Intent(this, CandidateActivity::class.java)
        startActivity(candidate)
    }
}

