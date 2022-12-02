package com.postive.bugiwear

import android.app.Activity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.postive.bugiwear.databinding.ActivityMyProfileBinding
import com.postive.bugiwear.post.ImageCacheManager

class MyProfileActivity : Activity() {

    private lateinit var binding: ActivityMyProfileBinding
    private val db = Firebase.firestore
    private val userDataCollectionRef = db.collection("userdata")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userDataCollectionRef.document(Firebase.auth.uid.toString()).get().addOnSuccessListener {
            binding.userName.text = it.get("name").toString()
            binding.email.text = it.get("email").toString()
            binding.phoneNumber.text = it.get("phone_number").toString()
        }
        binding.profileImage.setImageBitmap(ImageCacheManager.requestImage(Firebase.auth.uid.toString()))
    }
}