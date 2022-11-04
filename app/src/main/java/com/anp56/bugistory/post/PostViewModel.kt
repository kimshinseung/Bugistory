package com.anp56.bugistory.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    val postData = MutableLiveData<MutableList<PostData>>()

    fun addPostData(post : PostData){
        viewModelScope.launch {
            try {
                postData.value!!.add(post);
            }
            catch(_ : Exception){
                //ignored
            }
        }
    }
}