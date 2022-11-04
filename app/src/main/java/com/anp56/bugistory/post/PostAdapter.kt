package com.anp56.bugistory.post

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.anp56.bugistory.databinding.ItemPostRecyclerViewBinding

class PostAdapter(private val context : Context,private val posts : List<PostData>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

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
        holder.binding.commentCount.text = post.commentCount.toString()
        holder.binding.likeCount.text = post.likeCount.toString()

        holder.binding.likeButton.setOnClickListener {
            Toast.makeText(context,"좋아요 버튼이 눌렸습니다.",Toast.LENGTH_LONG).show()
        }

        holder.binding.commentButton.setOnClickListener {
            Toast.makeText(context,"댓글 창을 누르셨습니다.",Toast.LENGTH_LONG).show()
        }

    }

    override fun getItemCount(): Int {
        return posts.size;
    }
}