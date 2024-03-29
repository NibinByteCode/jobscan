package com.example.jobscan.adapters

import android.content.Context
import android.content.Intent
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
import com.example.jobscan.DetailActivity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeRecyclerAdapter(private val context: Context, options: FirebaseRecyclerOptions<PostData>) :
    FirebaseRecyclerAdapter<PostData, HomeRecyclerAdapter.MyViewHolder>(options) {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userprofileImage: ImageView = itemView.findViewById(R.id.imageViewUserProfile)
        val userName: TextView = itemView.findViewById(R.id.textViewUserName)
        val postImage: ImageView = itemView.findViewById(R.id.imageViewPostImage)
        val postContent: TextView = itemView.findViewById(R.id.textViewPostContent)
        val postDate: TextView = itemView.findViewById(R.id.textViewPostdate)
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
    try {
        if(model!=null) {
            holder.userName.text = ""
            holder.postContent.text = ""
            getUserData(model.userId) { userDetail ->
                Log.i("userData", model.userId.toString())
                if (userDetail != null) {
                    Log.i("userData", "User data retrieved successfully: $userDetail")
                    val storageReference =
                        FirebaseStorage.getInstance().getReferenceFromUrl(userDetail.profileImage)
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

            if (model.postImage == null) {
                holder.postImage.visibility = View.GONE // or View.INVISIBLE
            } else {
                Glide.with(holder.postImage.context)
                    .load(model.postImage)
                    .error(R.drawable.logo_user) // Set error placeholder image
                    .into(holder.postImage)
            }
            holder.postContent.text = model.postContent
            val timestamp = model.postDate // Your timestamp
            val date = Date(timestamp)
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(date)
            holder.postDate.text = formattedDate
            holder.itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("userId", model.userId)
                context.startActivity(intent)
            }
        }
    }
   catch (error:Exception){
       Log.i("Error_Nibin",error.toString())

   }
}


    private fun getUserData(userId: String, callback: (UserData?) -> Unit) {
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        val userQuery = reference.orderByChild("userId").equalTo(userId)
        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.children.firstOrNull()?.getValue(UserData::class.java)?.let { userData ->
                        println("User Data Retrieved: $userData")
                        callback(userData)
                    } ?: run {
                        // Handle case where desired child does not exist
                        println("User Data for user ID $userId does not exist")
                        callback(null)
                    }
                } else {
                    // Handle case where dataSnapshot does not contain any children
                    println("No user data found for user ID: $userId")
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                println("Database Error: ${databaseError.message}")
                callback(null)
            }
        })
    }
}
