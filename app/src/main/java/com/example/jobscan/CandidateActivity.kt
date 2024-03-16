package com.example.jobscan
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobscan.models.UserData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.Query

class CandidateActivity : AppCompatActivity() {
    private var adapter :CandidateAdapter? = null
    private var query: Query? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)
        supportActionBar?.apply {
            title = "Connections"
        }
        query = FirebaseDatabase.getInstance().reference.child("Users")
        val options = FirebaseRecyclerOptions.Builder<UserData>()
            .setQuery(query!!, UserData::class.java)
            .build()
        adapter = CandidateAdapter(this,options)

        val rView : RecyclerView = findViewById(R.id.rCandidateView)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter

        val searchView: SearchView = findViewById(R.id.candidate_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    Log.i("test", "Search done onsubmit $query")
                    searchCandidates(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    Log.i("test", "Search done onchange $newText")
                    searchCandidates(newText)
                }
                return true
            }
        })
    }

    private fun searchCandidates(queryText: String) {
        val searchQuery: Query = query!!.orderByChild("firstName")
            .startAt(queryText.toLowerCase())
            .endAt(queryText.toLowerCase() + "\uf8ff")

        val options = FirebaseRecyclerOptions.Builder<UserData>()
            .setQuery(searchQuery, UserData::class.java)
            .build()

        adapter?.updateOptions(options)
    }


    override fun onStart() {
        super.onStart()
        adapter?.startListening()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                startActivity(Intent(this@CandidateActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                })
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_connections -> {
                // No need to start the same activity again
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                startActivity(Intent(this@CandidateActivity, DetailActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}