package com.example.jobscan
import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobscan.models.UserData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import androidx.appcompat.app.AppCompatActivity
import com.example.jobscan.adapters.CandidateAdapter
import com.example.jobscan.helpers.BottomNavigationHandler
import com.google.android.material.bottomnavigation.BottomNavigationView

class CandidateActivity : AppCompatActivity() {
    private var adapter : CandidateAdapter? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var bottomNavigationHandler: BottomNavigationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)
        supportActionBar?.apply {
            title = "Connections"
        }
        // Initialize bottom navigation view
        bottomNavigationView = findViewById(R.id.nav_view)
        bottomNavigationHandler = BottomNavigationHandler(this)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            bottomNavigationHandler.onNavigationItemSelected(item.itemId)
        }
        bottomNavigationHandler.selectBottomNavigationItem(bottomNavigationView, R.id.navigation_connections)

        val query = FirebaseDatabase.getInstance().reference.child("Users")
        val options = FirebaseRecyclerOptions.Builder<UserData>().setQuery(query, UserData::class.java).build()
        adapter = CandidateAdapter(this,options)

        val rView : RecyclerView = findViewById(R.id.rCandidateView)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()

    }


}