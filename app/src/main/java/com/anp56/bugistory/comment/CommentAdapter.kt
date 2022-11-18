package com.anp56.bugistory.comment

import android.content.Context
import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anp56.bugistory.databinding.ItemCommentRecyclerViewBinding
import com.anp56.bugistory.databinding.ItemPostRecyclerViewBinding

class CommentAdapter(private val context : Context,private val comments : List<Pair<String,String>>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(){
    inner class CommentViewHolder(val binding : ItemCommentRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCommentRecyclerViewBinding.inflate(layoutInflater,parent,false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position];
        holder.binding.userName.text = comment.first
        holder.binding.commentContent.text = comment.second
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}