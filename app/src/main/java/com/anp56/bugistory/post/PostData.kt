package com.anp56.bugistory.post

data class PostData(
    val id : String,
    val username : String,
    val date : String,
    val context : String,
    val likeCount : Int,
    val commentCount : Int
)