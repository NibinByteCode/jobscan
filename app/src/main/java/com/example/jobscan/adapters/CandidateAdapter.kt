package com.example.jobscan.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jobscan.DetailActivity
import com.example.jobscan.R
import com.example.jobscan.models.UserData
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CandidateAdapter (private val context: Context, options: FirebaseRecyclerOptions<UserData>) :
    FirebaseRecyclerAdapter<UserData, CandidateAdapter.MyViewHolder>(options){

    // Initializing Firebase variables
    val currentUserID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val currentUserConnections: MutableSet<String> = mutableSetOf()

    init {
        // Fetching current user's connections once during initialization
        currentUserID?.let { userId ->
            FirebaseDatabase.getInstance().getReference("Users").child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val currentUserData = dataSnapshot.getValue(UserData::class.java)
                            currentUserData?.connections?.keys?.let { keys ->
                                currentUserConnections.addAll(keys)
                                notifyDataSetChanged()
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("CandidateAdapter", "Error fetching current user data: ${databaseError.message}")
                    }
                })
        }
    }

    // Inflate layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: UserData) {
        // Load profile image using Glide
        if (model.profileImage.isNotEmpty()) {
            val storRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.profileImage)
            Glide.with(holder.profileImage.context).load(storRef).into(holder.profileImage)
        } else {
            holder.profileImage.setImageResource(R.drawable.logo_user)
        }


        holder.userName.text = model.firstName + " "+ model.lastName
        holder.userType.text = model.userType
        holder.designation.text = model.designation

        // Check if the displayed user ID is in the connections of the current user
        val isConnected = currentUserConnections.contains(model.userId)
        holder.connectionStatus.text = if (isConnected) "Connected" else "Not Connected"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("userId", model.userId)
            context.startActivity(intent)
        }
    }

    // ViewHolder class to hold references to views in each item layout
    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.activity_candidate_row, parent, false)) {
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val userType: TextView = itemView.findViewById(R.id.user_type)
        val designation: TextView = itemView.findViewById(R.id.designation)
        val connectionStatus: TextView = itemView.findViewById(R.id.connection_status)
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
    }
}