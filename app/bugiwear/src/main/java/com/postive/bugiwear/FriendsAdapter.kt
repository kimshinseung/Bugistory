package com.postive.bugiwear

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.postive.bugiwear.databinding.ItemFriendRecyclerViewBinding
import com.postive.bugiwear.post.ImageCacheManager

class FriendsAdapter(private val friendList : MutableList<FriendData>) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>(){
    inner class FriendsViewHolder(val binding: ItemFriendRecyclerViewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFriendRecyclerViewBinding.inflate(layoutInflater,parent,false)
        return FriendsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val data: FriendData = friendList[position]
        holder.binding.itemName.text = data.name
        holder.binding.itemPhoneNumber.text = data.phonenumber
        ImageCacheManager.requestImage(data.profile,holder.binding.itemImage)
        holder.binding.profileButton.setOnClickListener {
            val intent = Intent(holder.itemView.context,UserPostListActivity::class.java)
            intent.putExtra("uid",data.profile)
            ContextCompat.startActivity(holder.itemView.context,intent,null)
        }
    }

    override fun getItemCount() = friendList.size
}