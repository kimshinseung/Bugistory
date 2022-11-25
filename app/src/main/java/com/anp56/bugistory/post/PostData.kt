package com.anp56.bugistory.post

data class PostData(
    val id: String,
    var uid : String,
    var username: String,
    val date: String,
    val content: String,
    val like: MutableList<String> = mutableListOf(),
    val comment: MutableList<Map<String,String>> = mutableListOf()
)