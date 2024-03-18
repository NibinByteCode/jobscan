package com.example.jobscan

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobscan.adapters.CandidateAdapter
import com.example.jobscan.helpers.BottomNavigationHandler
import com.example.jobscan.models.UserData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CandidateActivity : AppCompatActivity() {
    private var adapter: CandidateAdapter? = null
    private var query: Query? = null
    private var connectedUsersList: MutableList<String> = mutableListOf()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var bottomNavigationHandler: BottomNavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)
        //Setting the title of layout
        supportActionBar?.apply {
            title = "Connections"
        }

        //Bottom Navigation menu
        bottomNavigationView = findViewById(R.id.nav_view)
        bottomNavigationHandler = BottomNavigationHandler(this)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            bottomNavigationHandler.onNavigationItemSelected(item.itemId)
        }
        bottomNavigationHandler.selectBottomNavigationItem(
            bottomNavigationView,
            R.id.navigation_connections
        )

        //Fetching the current user id
        //val currentUserID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        query = FirebaseDatabase.getInstance().reference.child("Users")
        val options = FirebaseRecyclerOptions.Builder<UserData>()
            .setQuery(query!!, UserData::class.java)
            .build()
        adapter = CandidateAdapter(this, options)

        val rView: RecyclerView = findViewById(R.id.rCandidateView)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter


        val searchView: SearchView = findViewById(R.id.candidate_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    Log.i("test", "Search done onsubmit $query")

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val searchQuery = newText.trim()
                    if (searchQuery.isNotEmpty()) {
                        val formattedSearchQuery =
                            searchQuery.substring(0, 1).toUpperCase() + searchQuery.substring(1)
                                .toLowerCase()
                        val firebaseSearchQuery = (query as DatabaseReference).orderByChild("firstName")
                            .startAt(formattedSearchQuery)
                            .endAt(formattedSearchQuery + "\uf8ff")
                        val options = FirebaseRecyclerOptions.Builder<UserData>()
                            .setQuery(firebaseSearchQuery, UserData::class.java)
                            .build()
                        adapter?.updateOptions(options)
                        adapter?.notifyDataSetChanged()
                    } else {
                        // If search query is empty, show all users
                        val firebaseQuery = (query as DatabaseReference).orderByChild("firstName")
                        val options = FirebaseRecyclerOptions.Builder<UserData>()
                            .setQuery(firebaseQuery, UserData::class.java)
                            .build()
                        adapter?.updateOptions(options)
                        adapter?.notifyDataSetChanged()
                    }
                }
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
