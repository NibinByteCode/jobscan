package com.example.jobscan

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.FirebaseDatabase


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
        bottomNavigationHandler.selectBottomNavigationItem(
            bottomNavigationView,
            R.id.navigation_home
        )
        val rView: RecyclerView = findViewById(R.id.postRecycler)
        val query = FirebaseDatabase.getInstance().reference.child("Posts")
        val options = FirebaseRecyclerOptions.Builder<PostData>()
            .setQuery(query, PostData::class.java)
            .build()
        Log.i("data", "getting data")
        adapter = HomeRecyclerAdapter(this, options)
        val mmLinearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        mmLinearLayoutManager.reverseLayout = true
        mmLinearLayoutManager.setStackFromEnd(true)
        rView.layoutManager = mmLinearLayoutManager
        rView.adapter = adapter


        val createPostButton: FloatingActionButton = findViewById(R.id.addPost)
        createPostButton.setOnClickListener {
            val intent = Intent(this, CreatePost::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }


}
