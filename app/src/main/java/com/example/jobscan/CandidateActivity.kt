package com.example.jobscan

import android.os.Bundle
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
        supportActionBar?.apply {
            title = "Connections"
        }
        bottomNavigationView = findViewById(R.id.nav_view) // Correct initialization
        bottomNavigationHandler = BottomNavigationHandler(this)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            bottomNavigationHandler.onNavigationItemSelected(item.itemId)
        }
        bottomNavigationHandler.selectBottomNavigationItem(bottomNavigationView, R.id.navigation_connections)


        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid ?: ""


        query = FirebaseDatabase.getInstance().reference.child("Users")
        val options = FirebaseRecyclerOptions.Builder<UserData>()
            .setQuery(query!!, UserData::class.java)
            .build()
        adapter = CandidateAdapter(this, options)

        val rView: RecyclerView = findViewById(R.id.rCandidateView)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter


//        val searchView: SearchView = findViewById(R.id.candidate_search)
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (query != null) {
//                    Log.i("test", "Search done onsubmit $query")
//                    searchCandidates(query)
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText != null) {
//                    Log.i("test", "Search done onchange $newText")
//                    searchCandidates(newText)
//                }
//                return true
//            }
//        })
//    }
//
//    private fun searchCandidates(queryText: String) {
//        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
//
//        val query: Query = usersRef.orderByChild("firstName").startAt(queryText).endAt(queryText + "\uf8ff")
//
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val userList: MutableList<UserData> = mutableListOf()
//
//                for (snapshot in dataSnapshot.children) {
//                    val user = snapshot.getValue(UserData::class.java)
//                    if (user != null) {
//                        if (user.connections.isNotEmpty() && connectedUsersList.contains(user.userId)) {
//                            userList.add(0, user) // Add connected users to the beginning of the list
//                        } else {
//                            userList.add(user)
//                        }
//                    }
//                }
//
//                // Update the adapter with the sorted list
//                userList.sortBy { it.firstName } // Sort by first name
//                adapter?.updateUserList(userList)
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.e("CandidateActivity", "Error fetching users: ${databaseError.message}")
//            }
//        })
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
