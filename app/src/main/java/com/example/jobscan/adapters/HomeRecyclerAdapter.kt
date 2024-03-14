package com.example.jobscan.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jobscan.R
import com.example.jobscan.models.PostData
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class HomeRecyclerAdapter ( options: FirebaseRecyclerOptions<PostData>) :
    FirebaseRecyclerAdapter<PostData, HomeRecyclerAdapter.MyViewHolder>(options){

    class MyViewHolder (inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.post_layout, parent, false)){
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
        return MyViewHolder(inflater, parent)
    }
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int,
        model: PostData
    ) {
        Glide.with(holder.postImage.context).load(model.postImage).into(holder.postImage)
        holder.userName.text = model.userId
        holder.postContent.text = model.postContent

    }

}