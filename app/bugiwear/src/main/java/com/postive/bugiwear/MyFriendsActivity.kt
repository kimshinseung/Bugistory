package com.postive.bugiwear

import android.app.Activity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.postive.bugiwear.databinding.ActivityMyFriendsBinding

class MyFriendsActivity : Activity() {

    private lateinit var binding: ActivityMyFriendsBinding
    private val db = Firebase.firestore
    private val userdataCollectionRef = db.collection("userdata")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createFriendList()
    }
    private fun createFriendList(){
        val friendsList = mutableListOf<FriendData>()
        binding.friendList.adapter = FriendsAdapter(friendsList)
        userdataCollectionRef.document(Firebase.auth.uid.toString()).get()
            .addOnSuccessListener {
                val friendUidList = it.get("friends") as List<String>
                for (friendUid in friendUidList ){
                    userdataCollectionRef.document(friendUid).get().addOnSuccessListener { data ->
                        friendsList.add(
                            FriendData(
                                friendUid,
                                data.get("name").toString(),
                                data.get("phone_number").toString(),
                                data.get("email").toString()
                            )
                        )
                        binding.friendList.adapter = FriendsAdapter(friendsList)
                    }
                }
            }
    }
}