package com.anp56.bugistory.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ProfileDataViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val userDataCollectionRef = db.collection("userdata")
    var username = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var phoneNumber = MutableLiveData<String>()
    var friendList = MutableLiveData<MutableList<String>>()
    fun refreshData() {
        viewModelScope.launch {
            userDataCollectionRef.document(Firebase.auth.uid.toString()).get().addOnSuccessListener {
                username.value = it.get("name").toString()
                email.value = it.get("email").toString()
                phoneNumber.value = it.get("phone_number").toString()
                friendList.value = it.get("friends") as MutableList<String>
            }
        }
    }
    fun updatePhoneNumber(context : Context, number : String){
        viewModelScope.launch {
            phoneNumber.value = number
            userDataCollectionRef.document(Firebase.auth.uid.toString()).update("phone_number",number)
            Toast.makeText(context,"전화번호를 변경하였습니다.",Toast.LENGTH_SHORT).show()
        }
    }
    fun removeData(){
        viewModelScope.launch {
            username.value = ""
            email.value = ""
            phoneNumber.value = ""
            friendList.value = mutableListOf()
        }
    }
}