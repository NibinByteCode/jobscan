package com.example.jobscan

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jobscan.models.UserData
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CandidateAdapter ( options: FirebaseRecyclerOptions<UserData>) :
    FirebaseRecyclerAdapter<UserData, CandidateAdapter.MyViewHolder>(options){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CandidateAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(
        holder: CandidateAdapter.MyViewHolder,
        position: Int,
        model: UserData
    ) {
        val storRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.profileImage)
        Glide.with(holder.profileImage.context).load(storRef).into(holder.profileImage)

        holder.userName.text = model.firstName + " " +model.lastName
        holder.designation.text = model.designation
        holder.connectionStatus.text = "Connect"
    }

    class MyViewHolder (inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.activity_candidate_row, parent, false)){
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val designation: TextView = itemView.findViewById(R.id.designation)
        val connectionStatus: TextView = itemView.findViewById(R.id.connection_status)
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
    }
}