package com.anp56.bugistory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.anp56.bugistory.databinding.FragmentMainBinding
import com.anp56.bugistory.post.PostAdapter
import com.anp56.bugistory.post.PostData
import com.anp56.bugistory.post.PostViewModel

class MainFragment : Fragment() {
    lateinit var mainContext : Context;
    lateinit var postViewModel : PostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMainBinding.bind(view)
        val posts = mutableListOf<PostData>()
        for (i in 1..10){
            println("Doing here")
            var count = i
            var cont = ""
            for (j in 0..count){
                cont += "글이 얼마나 적힐지 테스트"
            }
            posts.add(
                PostData(
                    0,
                    "유저 고유 아이디 번호가 들어간다",
                    "테스트용 $count",
                    "2022년 11월 04시 7시 ${count}분",
                    cont,
                    count,
                    count
                )
            )
        }

        val adapter = PostAdapter(mainContext, posts)
        binding.postRecyclerView.adapter = adapter

        binding.createPost.setOnClickListener {
            val intent = Intent(activity,ChatActivity::class.java)
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