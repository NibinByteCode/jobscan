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

class CandidateAdapter (private val context: Context, options: FirebaseRecyclerOptions<UserData>) :
    FirebaseRecyclerAdapter<UserData, CandidateAdapter.MyViewHolder>(options){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int,
        model: UserData
    ) {
//        val storRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.profileImage)
        Glide.with(holder.profileImage.context).load(model.profileImage).into(holder.profileImage)

        holder.userName.text = model.firstName
        holder.designation.text = model.designation
        holder.connectionStatus.text = "Connect"

        holder.itemView.setOnClickListener {
//            val userId = getItem(position)?.userId
//            userId?.let {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("userId", model.userId)
                context.startActivity(intent)
//            }
        }
    }

    class MyViewHolder (inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.activity_candidate_row, parent, false)){
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val designation: TextView = itemView.findViewById(R.id.designation)
        val connectionStatus: TextView = itemView.findViewById(R.id.connection_status)
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
    }
}