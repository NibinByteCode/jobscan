package com.example.jobscan.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.jobscan.R
import com.example.jobscan.models.PostData
import com.example.jobscan.models.UserData
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeRecyclerAdapter(options: FirebaseRecyclerOptions<PostData>) :
    FirebaseRecyclerAdapter<PostData, HomeRecyclerAdapter.MyViewHolder>(options) {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userprofileImage: ImageView = itemView.findViewById(R.id.imageViewUserProfile)
        val userName: TextView = itemView.findViewById(R.id.textViewUserName)
        val likeImage: ImageView = itemView.findViewById(R.id.imageViewLike)
        val dislikeImage: ImageView = itemView.findViewById(R.id.imageViewDislike)
        val postImage: ImageView = itemView.findViewById(R.id.imageViewPostImage)
        val postContent: TextView = itemView.findViewById(R.id.textViewPostContent)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.post_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int,
        model: PostData
    ) {
        getUserData(model.userId) { userDetail ->
            Log.i("userData",model.userId.toString())
            Glide.with(holder.userprofileImage.context).load(userDetail.profileImage).apply(
                RequestOptions().transform(CircleCrop())).into(holder.userprofileImage)
            holder.userName.text = "${userDetail.firstName} ${userDetail.lastName}"
            Glide.with(holder.postImage.context).load(model.postImage).into(holder.postImage)
            holder.postContent.text = model.postContent
        }
    }


    private fun getUserData(userId: String, callback: (UserData) -> Unit) {
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        val userQuery = reference.orderByChild("userId").equalTo(userId)
        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.children.first().getValue(UserData::class.java)
                val postUserData = userData ?: UserData("Deleted", "User")
                println("User Data Retrieved: $postUserData")
                callback(postUserData)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                println("Database Error: ${databaseError.message}")
            }
        })
    }
}
