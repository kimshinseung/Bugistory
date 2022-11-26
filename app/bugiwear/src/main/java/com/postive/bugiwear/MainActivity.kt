package com.postive.bugiwear

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.postive.bugiwear.post.PostAdapter
import com.postive.bugiwear.post.PostData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.postive.bugiwear.databinding.ActivityMainBinding
import com.postive.bugiwear.post.ImageCacheManager

class MainActivity : Activity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val  postCollectionRef = db.collection("post")
    private val userCollectionRef = db.collection("userdata")
    private lateinit var binding: ActivityMainBinding
    private val postDataList = mutableListOf<PostData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.postList.adapter = PostAdapter(this, mutableListOf())
        updatePostList()
        binding.settingButton.setOnClickListener {
            startActivity(Intent(this,SettingActivity::class.java))
        }
        binding.refreshButton.setOnClickListener {
            updatePostList()
        }
    }

    override fun onResume() {
        super.onResume()
        ImageCacheManager.updateImage()
    }
    private fun updatePostList(){

        postCollectionRef.whereIn("see", arrayListOf("-ALL-", Firebase.auth.uid)).get().addOnSuccessListener {
            val postList = mutableListOf<PostData>()
            for (data in it){
                try {
                    val postData = PostData(
                        data.id,
                        data.get("uid").toString(),
                        "알수없음",
                        data.get("time").toString(),
                        data.get("content").toString(),
                        (data.get("like") as MutableList<String>),
                        (data.get("comment") as MutableList<Map<String,String>>)
                    )
                    userCollectionRef.document(data.get("uid").toString()).get()
                        .addOnSuccessListener { userdata ->
                            postData.username = userdata["name"].toString()
                            postList.add(postData)
                            postList.sortByDescending { it -> it.date }
                            binding.postList.adapter = PostAdapter(this,postList)
                        }
                        .addOnFailureListener {
                            postList.add(postData)
                            postList.sortByDescending { it -> it.date }
                            binding.postList.adapter = PostAdapter(this,postList)
                        }
                }
                catch (_ : Exception){
                }
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }
}