package com.anp56.bugistory.post

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.anp56.bugistory.PostViewerActivity
import com.anp56.bugistory.R
import com.anp56.bugistory.databinding.ItemPostRecyclerViewBinding
import com.anp56.bugistory.tools.ImageCacheManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(private val context : Context,private val posts : List<PostData>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val storage = Firebase.storage
    private val postCollectionRef = db.collection("post")
    private val userCollectionRef = db.collection("userdata")
    inner class PostViewHolder(val binding : ItemPostRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPostRecyclerViewBinding.inflate(layoutInflater)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        val formatter = SimpleDateFormat("YYYY년 MM월 dd일 HH시 mm분")
        holder.binding.userName.text = post.username
        holder.binding.dateText.text = formatter.format(Date(post.date.toLong()))
        holder.binding.postContent.text = post.content
        holder.binding.commentCount.text = post.comment.size.toString()
        holder.binding.likeCount.text = post.like.size.toString()
        ImageCacheManager.requestImage(post.uid,holder.binding.userImage)
        //글 자세히 보기 버튼
        holder.binding.postContent.setOnClickListener {
            PostViewerActivity.currentPost = post
            context.startActivity(Intent(context,PostViewerActivity::class.java))
        }
        //좋아요 버튼 눌렸을 때 처리
        holder.binding.likeButton.setOnClickListener {
            val uid = Firebase.auth.uid.toString()
            if (post.like.contains(uid)){
                Toast.makeText(context,"이미 좋아요하신 글 입니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            post.like.add(uid)
            postCollectionRef.document(post.id).update("like",post.like)
                .addOnSuccessListener {
                    Toast.makeText(context,"이 글을 좋아요 하셨습니다.",Toast.LENGTH_SHORT).show()
                    holder.binding.likeCount.text = post.like.size.toString()
                }
                .addOnFailureListener {
                    Toast.makeText(context,"좋아요에 실패했습니다.",Toast.LENGTH_SHORT).show()
                    post.like.remove(uid)
                    holder.binding.likeCount.text = post.like.size.toString()
                }
        }

        holder.binding.commentButton.setOnClickListener {
            PostViewerActivity.currentPost = post
            context.startActivity(Intent(context,PostViewerActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return posts.size;
    }
}