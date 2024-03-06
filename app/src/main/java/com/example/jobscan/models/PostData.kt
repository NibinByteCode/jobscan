package com.example.jobscan.models

data class PostData(
    var postId: String = "",
    var postContent: String = "",
    var postImage: String? = null,
    var userId: String = "",
    var likeCount: Int = 0,
    var dislikeCount: Int = 0
){
}