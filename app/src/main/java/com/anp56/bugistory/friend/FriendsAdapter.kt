package com.anp56.bugistory.friend

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.anp56.bugistory.FriendProfileActivity
import com.anp56.bugistory.R
import com.anp56.bugistory.databinding.ItemFriendBinding
import com.anp56.bugistory.databinding.ItemPostRecyclerViewBinding
import com.anp56.bugistory.post.PostData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_friend.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class Data (val profile :String, val name : String, val phonenumber : String, val email : String)


class FriendsAdapter(private var friendsData:MutableList<Data>) : RecyclerView.Adapter<FriendsAdapter.CustomViewHolder>(){

    class CustomViewHolder(val binding : ItemFriendBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFriendBinding.inflate(layoutInflater, parent,false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val data =  friendsData[position]
        //holder.profile_picture.setImageResource(data.profile)
        holder.binding.itemName.text = data.name
        holder.binding.itemPhonenumber.text = data.phonenumber
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView?.context, FriendProfileActivity::class.java)
            intent.putExtra("name",data.name)
            intent.putExtra("profile",data.profile)
            intent.putExtra("phonenumber",data.phonenumber)
            intent.putExtra("Email",data.email)
            ContextCompat.startActivity(holder.itemView.context, intent, null)

        }
    }

    override fun getItemCount() = friendsData.size
}
