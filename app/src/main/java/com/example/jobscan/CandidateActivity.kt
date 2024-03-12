package com.example.jobscan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobscan.models.UserData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class CandidateActivity : AppCompatActivity() {
    private var adapter :CandidateAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)

        val query = FirebaseDatabase.getInstance().reference.child("Users")
        val options = FirebaseRecyclerOptions.Builder<UserData>().setQuery(query, UserData::class.java).build()
        adapter = CandidateAdapter(options)

        val rView : RecyclerView = findViewById(R.id.rCandidateView)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }
}