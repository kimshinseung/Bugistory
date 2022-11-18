package com.anp56.bugistory

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anp56.bugistory.databinding.ActivityProfileFriendBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FriendProfileActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val userCollectionRef = db.collection("userdata")
    private val binding by lazy { ActivityProfileFriendBinding.inflate(layoutInflater) }
    private val isFriend = false
    private var uid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val name = intent.getStringExtra("name")
        val phoneNumber = intent.getStringExtra("phonenumber")

        val email = intent.getStringExtra("Email")
        //binding.profileimage.setImageResource(profile)
        uid = intent.getStringExtra("profile") + ""


        binding.phone.text = phoneNumber
        binding.name.text = name
        binding.email.text = email

        binding.friendButton.setImageResource(if(isFriend) R.mipmap.friend_minus_button else R.mipmap.friend_add_button)

        userCollectionRef.document(uid+"").get()
            .addOnSuccessListener {
                try {
                    binding.email.text = it.get("email").toString()
                    binding.name.text = it.get("name").toString()
                    binding.phone.text = it.get("phone_number").toString()
                }
                catch (_ : Exception){
                    //ignored
                    Toast.makeText(this,"프로필 정보를 불러오는데 실패했습니다.",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"프로필 정보를 불러오는데 실패했습니다.",Toast.LENGTH_SHORT).show()
            }

        binding.backButton.setOnClickListener{
            super.onBackPressed()
        }

        binding.chatButton.setOnClickListener {

        }

        binding.friendButton.setOnClickListener {

        }

        binding.friendPostButton.setOnClickListener {
            val myIntent = Intent(this, FriendPost::class.java)
            myIntent.putExtra("uid", uid)
            startActivity(myIntent)
        }
    }
}