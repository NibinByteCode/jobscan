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
import com.google.firebase.storage.FirebaseStorage

class HomeRecyclerAdapter(options: FirebaseRecyclerOptions<PostData>) :
    FirebaseRecyclerAdapter<PostData, HomeRecyclerAdapter.MyViewHolder>(options) {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userprofileImage: ImageView = itemView.findViewById(R.id.imageViewUserProfile)
        val userName: TextView = itemView.findViewById(R.id.textViewUserName)
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
    holder.userName.text = ""
    holder.postContent.text = ""

    getUserData(model.userId) { userDetail ->
        Log.i("userData", model.userId.toString())
        if (userDetail != null) {
            Log.i("userData", "User data retrieved successfully: $userDetail")
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(userDetail.profileImage)
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                // Load the image using Glide with the generated download URL
                Glide.with(holder.userprofileImage.context)
                    .load(uri)
                    .apply(RequestOptions().transform(CircleCrop()))
                    .error(R.drawable.logo_user) // Set error placeholder image
                    .into(holder.userprofileImage)
            }.addOnFailureListener { exception ->
                Log.e("Glide", "Failed to generate download URL: ${exception.message}")
            }

            holder.userName.text = "${userDetail.firstName} ${userDetail.lastName}"
        } else {
            Log.e("userData", "Failed to retrieve user data for user ID: ${model.userId}")
        }
    }

    Glide.with(holder.postImage.context)
        .load(model.postImage)
        .error(R.drawable.logo_user) // Set error placeholder image
        .into(holder.postImage)
    holder.postContent.text = model.postContent
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
