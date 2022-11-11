package com.anp56.bugistory.post

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.anp56.bugistory.PostViewerActivity
import com.anp56.bugistory.databinding.ItemPostRecyclerViewBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostAdapter(private val context : Context,private val posts : List<PostData>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val  postCollectionRef = db.collection("post")
    inner class PostViewHolder(val binding : ItemPostRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPostRecyclerViewBinding.inflate(layoutInflater)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]



        holder.binding.userName.text = post.username
        holder.binding.dateText.text = post.date
        holder.binding.postContent.text = post.content
        holder.binding.commentCount.text = post.comment.size.toString()
        holder.binding.likeCount.text = post.like.size.toString()

        //글 자세히 보기 버튼
        holder.binding.postContent.setOnClickListener {
            PostViewerActivity.currentPostIndex = position
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
            Toast.makeText(context,"댓글 창을 누르셨습니다.",Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return posts.size;
    }
}