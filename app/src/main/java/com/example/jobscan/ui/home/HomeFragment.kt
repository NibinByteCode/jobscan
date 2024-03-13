package com.example.jobscan.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobscan.adapters.HomeRecyclerAdapter
import com.example.jobscan.databinding.FragmentHomeBinding
import com.example.jobscan.models.PostData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment() {
    private var adapter: HomeRecyclerAdapter? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        Log.i("data","inside")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val rView: RecyclerView = binding.postRecycler
        val searchView: SearchView = binding.searchView
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
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val query = FirebaseDatabase.getInstance().reference.child("Posts")
        val options = FirebaseRecyclerOptions.Builder<PostData>()
            .setQuery(query, PostData::class.java)
            .build()
        Log.i("data","getting data")
        adapter = HomeRecyclerAdapter(options)
        binding.postRecycler.layoutManager = LinearLayoutManager(activity)
        binding.postRecycler.adapter = adapter

    }
    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
