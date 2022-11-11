package com.anp56.bugistory

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class MainFragment : Fragment() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val  postCollectionRef = db.collection("post")
    private val userCollectionRef = db.collection("userdata")
    lateinit var mainContext : Context;
    lateinit var postViewModel : PostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMainBinding.bind(view)
        val posts = mutableListOf<PostData>()
        postCollectionRef.get().addOnSuccessListener {
            //var formatter = SimpleDateFormat("YYYY년 MM월 DD일 HH시 mm분")
            for (data in it){
//                var username : String? = null;
//                userCollectionRef.document(data.get("uid").toString()).get()
//                    .addOnSuccessListener { userdata ->
//                        username = userdata.get("username").toString()
//                    }
//                    .addOnFailureListener {
//                        username = null
//                    }
//                if (username == null)
//                    continue
                val postData = PostData(
                    data.id.toString(),
                    data.get("uid").toString(),
                    data.get("time").toString(),
                    data.get("content").toString(),
                    (data.get("like") as MutableList<*>).size,
                    (data.get("comment") as HashMap<*, *>).size
                )
                posts.add(postData)
            }
        }
            .addOnFailureListener {
                Toast.makeText(activity,"불러오는데 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
        val adapter = PostAdapter(mainContext, posts)
        binding.postRecyclerView.adapter = adapter

        binding.createPost.setOnClickListener {
            val intent = Intent(activity,PostActivity::class.java)
            startActivity(intent)
        }
//        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]
//        postViewModel.postData.observe(viewLifecycleOwner){ posts ->
//            val adapter = PostAdapter(mainContext,posts)
//            binding.postRecyclerView.adapter = adapter
//        }
//        var count = 0;
//        binding.createPost.setOnClickListener {
//            var cont = ""
//            for (i in 0..count){
//                cont += "글이 얼마나 적힐지 테스트"
//            }
//            postViewModel.addPostData(
//                PostData(
//                    0,
//                    "유저 고유 아이디 번호가 들어간다",
//                    "테스트용 $count",
//                    "2022년 11월 04시 7시 ${count}분",
//                    cont,
//                    count,
//                    count
//                )
//            )
//            count++;
//        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mainContext = container!!.context;
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}