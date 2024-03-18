package com.example.jobscan
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobscan.adapters.HomeRecyclerAdapter
import com.example.jobscan.helpers.BottomNavigationHandler
import com.example.jobscan.models.PostData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private var adapter: HomeRecyclerAdapter? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var bottomNavigationHandler: BottomNavigationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        supportActionBar?.apply {
            title = "Home"
        }
        bottomNavigationView = findViewById(R.id.nav_view) // Correct initialization
        bottomNavigationHandler = BottomNavigationHandler(this)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            bottomNavigationHandler.onNavigationItemSelected(item.itemId)
        }
        bottomNavigationHandler.selectBottomNavigationItem(bottomNavigationView, R.id.navigation_home)
        val rView: RecyclerView = findViewById(R.id.postRecycler)
        val searchView: SearchView = findViewById(R.id.searchView)
        val query = FirebaseDatabase.getInstance().reference.child("Posts")
        val options = FirebaseRecyclerOptions.Builder<PostData>()
            .setQuery(query, PostData::class.java)
            .build()
        Log.i("data","getting data")
        adapter = HomeRecyclerAdapter(options)
        val mmLinearLayoutManager:LinearLayoutManager= LinearLayoutManager(this)
        mmLinearLayoutManager.reverseLayout=true
        mmLinearLayoutManager.setStackFromEnd(true)
        rView.layoutManager = mmLinearLayoutManager
        rView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.i("test", "Search done")
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val searchQuery = newText.trim() // Normalize the search query to lowercase
                val firebaseQuery = FirebaseDatabase.getInstance().reference.child("Posts")
                    .orderByChild("postContent")
                    .startAt(searchQuery)
                    .endAt("$searchQuery\uf8ff")

                val options = FirebaseRecyclerOptions.Builder<PostData>()
                    .setQuery(firebaseQuery, PostData::class.java)
                    .build()
                adapter?.updateOptions(options)
                adapter?.notifyDataSetChanged()
                return true
            }


        })


    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }


}
