package com.anp56.bugistory

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anp56.bugistory.databinding.ActivityFriendpostBinding
import com.anp56.bugistory.post.PostAdapter
import com.anp56.bugistory.post.PostData
import com.anp56.bugistory.post.PostViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FriendPost : AppCompatActivity(){
    lateinit var postViewModel : PostViewModel
    private val db: FirebaseFirestore = Firebase.firestore
    lateinit var storage: FirebaseStorage
    private val  postCollectionRef = db.collection("post")
    private val userCollectionRef = db.collection("userdata")
    private val userdataCollectionRef = db.collection("userdata")
    private val binding by lazy { ActivityFriendpostBinding.inflate(layoutInflater) }
    lateinit var frienduid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val secondIntent = intent

        frienduid=secondIntent.getStringExtra("uid")+""

        //실험할려고 만든 코드
        Toast.makeText(this,frienduid,Toast.LENGTH_SHORT).show()

        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        val adapter = PostAdapter(this, mutableListOf())
        binding.postRecyclerView.adapter = adapter
        updatePostList()

        postViewModel.postData.observe(this) {
            binding.postRecyclerView.adapter = PostAdapter(this, it)
        }

        binding.swipeLayout.setOnRefreshListener {
            updatePostList()
            binding.swipeLayout.isRefreshing = false
        }



        binding.backButton.setOnClickListener{
            super.onBackPressed()
        }
    }

    private fun updatePostList(){
        postCollectionRef.whereEqualTo("uid",frienduid).get().addOnSuccessListener {
            val postList = mutableListOf<PostData>()
            for (data in it){
                try {
                    val postData = PostData(
                        data.id,
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
                            postViewModel.setPostList(postList)
                        }
                        .addOnFailureListener {
                            postList.add(postData)
                            postViewModel.setPostList(postList)
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