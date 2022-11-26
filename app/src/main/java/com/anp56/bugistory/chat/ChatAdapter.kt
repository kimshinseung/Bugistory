package com.anp56.bugistory.chat

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.anp56.bugistory.databinding.ItemMyChatRecyclerViewBinding
import com.anp56.bugistory.databinding.ItemOpponentChatRecyclerViewBinding
import com.anp56.bugistory.databinding.ItemPostRecyclerViewBinding
import com.anp56.bugistory.tools.ImageCacheManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ChatAdapter(private val context: Context,private val chats : List<ChatData>) : RecyclerView.Adapter<ViewHolder>() {
    private val storage = Firebase.storage
    inner class OpponentChatViewHolder(val binding : ItemOpponentChatRecyclerViewBinding) : ViewHolder(binding.root)
    inner class MyChatViewHolder(val binding : ItemMyChatRecyclerViewBinding) : ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        val chat = chats[position]
        return if (chat.isOpponent) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (viewType == 0){
            val binding = ItemMyChatRecyclerViewBinding.inflate(layoutInflater,parent,false)
            return MyChatViewHolder(binding)
        }
        val binding = ItemOpponentChatRecyclerViewBinding.inflate(layoutInflater,parent,false)
        return OpponentChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]
        if (holder.itemViewType == 0){
            val viewHolder = holder as MyChatViewHolder
            viewHolder.binding.chatContent.text = chat.chatContent
            return
        }
        val viewHolder = holder as OpponentChatViewHolder
        viewHolder.binding.chatContent.text = chat.chatContent
        viewHolder.binding.opponentName.text = chat.userName
        ImageCacheManager.requestImage(chat.userId,holder.binding.chatOpponentImage)

    }

    override fun getItemCount(): Int {
        return chats.size
    }
}