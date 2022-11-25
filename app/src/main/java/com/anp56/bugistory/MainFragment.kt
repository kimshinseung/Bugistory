package com.anp56.bugistory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.anp56.bugistory.databinding.FragmentMainBinding
import com.anp56.bugistory.post.PostAdapter
import com.anp56.bugistory.post.PostData
import com.anp56.bugistory.post.PostViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainFragment : Fragment() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val  postCollectionRef = db.collection("post")
    private val userCollectionRef = db.collection("userdata")
    lateinit var mainContext : Context;
    lateinit var postViewModel : PostViewModel
    lateinit var binding: FragmentMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        val adapter = PostAdapter(mainContext, mutableListOf())
        binding.postRecyclerView.adapter = adapter

        updatePostList()

        postViewModel.postData.observe(viewLifecycleOwner) {
            binding.postRecyclerView.adapter = PostAdapter(mainContext, it)
        }

        binding.swipeLayout.setOnRefreshListener {
            updatePostList()
            binding.swipeLayout.isRefreshing = false
        }
        binding.createPost.setOnClickListener {
            val intent = Intent(activity,PostActivity::class.java)
            startActivity(intent)
        }
    }
    //Update post list and query data
    private fun updatePostList(){
        postCollectionRef.get().addOnSuccessListener {
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
                            postViewModel.setPostList(postList)
                        }
                        .addOnFailureListener {
                            postList.add(postData)
                            postList.sortByDescending { it -> it.date }
                            postViewModel.setPostList(postList)
                        }
                }
                catch (_ : Exception){
                    Log.d("Update Post","Post data parse failed.")
                }
            }
        }
            .addOnFailureListener {
                Toast.makeText(activity, "불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onResume() {
        super.onResume()
        updatePostList()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mainContext = container!!.context;
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}