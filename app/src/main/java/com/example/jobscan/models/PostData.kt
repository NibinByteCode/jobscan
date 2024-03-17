package com.example.jobscan.models

data class PostData(
    var postId: String = "",
    var postContent: String = "",
    var postImage: String? = null,
    var userId: String = "",
    var likeCount: Int = 0,
    var dislikeCount: Int = 0
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "postId" to postId,
            "postContent" to postContent,
            "postImage" to postImage,
            "userId" to userId,
            "likeCount" to likeCount,
            "dislikeCount" to dislikeCount
        )
    }
}