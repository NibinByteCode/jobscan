package com.example.jobscan

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobscan.helpers.BottomNavigationHandler
import com.example.jobscan.models.UserData
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
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var bottomNavigationHandler: BottomNavigationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.apply {
            title = "Profile"
        }
        bottomNavigationView = findViewById(R.id.nav_view)
        bottomNavigationHandler = BottomNavigationHandler(this)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            bottomNavigationHandler.onNavigationItemSelected(item.itemId)
        }
        bottomNavigationHandler.selectBottomNavigationItem(
            bottomNavigationView,
            R.id.navigation_profile
        )
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")
        val currentUser: FirebaseUser? = auth.currentUser
        currentUser?.uid?.let { userId ->
            val tempUserid ="0cPYokz5OAdEWGcGgrb2oikLtvp1"
            database.child(tempUserid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData = snapshot.getValue(UserData::class.java)
                        userData ?.firstName?.let { Log.i("User Data", it) }
                        userData?.let { user ->
                            findViewById<TextView>(R.id.userName).text = "${user.firstName} ${user.lastName}"
                            findViewById<TextView>(R.id.userEmail).text = user.email
                            findViewById<TextView>(R.id.userContact).text = user.phoneNumber

                            // Set other UI elements if needed
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle cancelled event
                }
            })
        }


    }
}