package com.anp56.bugistory.friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anp56.bugistory.R
import kotlinx.android.synthetic.main.item_friend.view.*

class Data (val profile :Int, val name : String, val phonenumber : String)

class CustomViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val profile = v.item_image
    val name = v.item_name
    val phonenumber = v.item_phonenumber
}

class FriendsAdapter(val DataList:ArrayList<Data>) : RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val cellForRow = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent,false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.profile.setImageResource(DataList[position].profile)
        holder.name.text = DataList[position].name
        holder.phonenumber.text = DataList[position].phonenumber

    }


    override fun getItemCount() = DataList.size
}