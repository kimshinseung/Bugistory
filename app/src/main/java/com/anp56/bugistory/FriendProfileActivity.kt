package com.anp56.bugistory

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anp56.bugistory.databinding.ActivityProfileFriendBinding
import com.anp56.bugistory.tools.ImageCacheManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_profile.*

class FriendProfileActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val userCollectionRef = db.collection("userdata")
    private val storage = Firebase.storage
    private val binding by lazy { ActivityProfileFriendBinding.inflate(layoutInflater) }
    private var isFriend = false
    private var uid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val name = intent.getStringExtra("name")
        val phoneNumber = intent.getStringExtra("phonenumber")

        val email = intent.getStringExtra("Email")
        uid = intent.getStringExtra("profile") + ""


        binding.phone.text = phoneNumber
        binding.name.text = name
        binding.email.text = email
        Toast.makeText(this,uid,Toast.LENGTH_SHORT).show()

        binding.profileImage.setImageResource(R.mipmap.default_user_image)
        ImageCacheManager.requestImage(uid,binding.profileImage)
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

        userCollectionRef.document(Firebase.auth.uid.toString()).get()
            .addOnSuccessListener {
                try {
                    val friendList = it.get("friends") as MutableList<String>
                    isFriend = friendList.contains(uid)
                    binding.friendButton.setImageResource(if(isFriend) R.mipmap.friend_minus_button else R.mipmap.friend_add_button)
                }
                catch (e : Exception){
                    //ignored
                    e.printStackTrace()
                    Toast.makeText(this,"프로필 정보를 불러오는데 실패했습니다.",Toast.LENGTH_SHORT).show()
                }
            }



        binding.backButton.setOnClickListener{
            super.onBackPressed()
        }

        binding.friendButton.setOnClickListener {

            userCollectionRef.document(Firebase.auth.uid.toString()).get()
                .addOnSuccessListener {
                    try {
                        val friendList = it.get("friends") as MutableList<String>
                        if (!isFriend) friendList.add(uid) else friendList.remove(uid)
                        userCollectionRef.document(Firebase.auth.uid.toString()).update("friends",friendList)
                        isFriend = !isFriend
                        binding.friendButton.setImageResource(if(isFriend) R.mipmap.friend_minus_button else R.mipmap.friend_add_button)
                        Toast.makeText(this,(if (isFriend) "친구가 되었습니다." else "친구가 해제되었습니다."),Toast.LENGTH_SHORT).show()
                    }
                    catch (_ : Exception){
                        //ignored
                        Toast.makeText(this,"친구 설정에 실패했습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.friendPostButton.setOnClickListener {
            val myIntent = Intent(this, FriendPost::class.java)
            myIntent.putExtra("uid", uid)
            startActivity(myIntent)
        }
    }
}