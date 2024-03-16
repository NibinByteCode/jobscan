package com.example.jobscan
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobscan.CandidateActivity
import com.example.jobscan.DetailActivity
import com.example.jobscan.adapters.HomeRecyclerAdapter
import com.example.jobscan.models.PostData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private var adapter: HomeRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        supportActionBar?.apply {
            title = "Home"
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val rView: RecyclerView = findViewById(R.id.postRecycler)
        val searchView: SearchView = findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.i("test", "Search done")
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.i("test", "Search done")
                return true
            }

        })
        val query = FirebaseDatabase.getInstance().reference.child("Posts")
        val options = FirebaseRecyclerOptions.Builder<PostData>()
            .setQuery(query, PostData::class.java)
            .build()
        Log.i("data","getting data")
        adapter = HomeRecyclerAdapter(options)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter


    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_connections -> {
                startActivity(Intent(this@MainActivity, CandidateActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
