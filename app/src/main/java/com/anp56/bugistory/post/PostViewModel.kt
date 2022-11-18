package com.anp56.bugistory.post

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    val postData = MutableLiveData<MutableList<PostData>>()
    private val postDataBase = mutableListOf<PostData>()
    fun addPost(post : PostData){
        viewModelScope.launch {
            try {
                postDataBase.add(post)
            }
            catch(_ : Exception){
                //ignored
                Log.d("add post","Post added")
            }
            postData.value = postDataBase
        }
    }
    fun removePost(index : Int){
        viewModelScope.launch {
            try {
                postDataBase.removeAt(index)
            }
            catch (_ : Exception){
                //ignored
            }
            postData.value = postDataBase
        }
    }
    fun setPostList(postList : MutableList<PostData>){
        viewModelScope.launch {
            postData.value = postList
        }
    }
    fun getPostByIndex(index: Int) : PostData?{
        var post : PostData? = null
        viewModelScope.launch {
            post = postData.value?.get(index)
        }
        return post
    }
}