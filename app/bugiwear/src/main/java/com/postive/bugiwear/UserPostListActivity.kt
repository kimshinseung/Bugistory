package com.postive.bugiwear

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.postive.bugiwear.databinding.ActivityUserPostListBinding
import com.postive.bugiwear.post.PostAdapter
import com.postive.bugiwear.post.PostData

class UserPostListActivity : Activity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val  postCollectionRef = db.collection("post")
    private val userCollectionRef = db.collection("userdata")
    private lateinit var binding: ActivityUserPostListBinding
    lateinit var frienduid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPostListBinding.inflate(layoutInflater)
        frienduid = intent.getStringExtra("uid")+""
        setContentView(binding.root)
        updatePostList()
    }
    private fun updatePostList(){
        postCollectionRef.whereEqualTo("uid",frienduid).whereEqualTo("see","-ALL-").get().addOnSuccessListener {
            val postList = mutableListOf<PostData>()
            for (data in it){
                try {
                    val postData = PostData(
                        data.id,
                        data.get("uid").toString(),
                        "알 수 없음",
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
                    Log.d("Update Post","Post data parse failed.")
                }

            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            }
    }
}