package com.anp56.bugistory.friend

import android.content.Intent
import android.graphics.BitmapFactory
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.item_friend.view.*
import kotlin.collections.ArrayList


class FriendsAdapter(
    private var DataList:MutableList<FriendData>
) : RecyclerView.Adapter<FriendsAdapter.CustomViewHolder>(), Filterable {

    var current: List<FriendData>? = null
    val storage = Firebase.storage
    inner class CustomViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val profile_picture : ImageView = v.item_image
        val friend_name : TextView = v.item_name
        val phonenum : TextView = v.item_phonenumber
        val friend_email : TextView? = null

    }

    init {
        this.current = DataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_friend, parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val data: FriendData = current!![position]
        //holder.profile_picture.setImageResource(data.profile)
        holder.friend_name.text = data.name
        holder.phonenum.text = data.phonenumber
        holder.friend_email?.text = data.email
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView?.context, FriendProfileActivity::class.java)
            intent.putExtra("name",data.name)
            intent.putExtra("profile",data.profile)
            intent.putExtra("phonenumber",data.phonenumber)
            intent.putExtra("Email",data.email)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
        val imageRef=storage.getReferenceFromUrl("gs://bugistory.appspot.com/photo/${data.profile}.png")
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            holder.profile_picture.setImageBitmap(bmp)
        }

    }

    override fun getItemCount() = current!!.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isNullOrBlank()) {
                    current = DataList
                } else {
                    val filteredList = ArrayList<FriendData>()

                    for (data in DataList){
                        if(data.name.contains(charString) || data.phonenumber.contains(charString)||data.email.contains(charString)) {
                            filteredList.add(data)
                        }
                    }
                    current = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = current

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
                current = filterResults?.values as ArrayList<FriendData>
                notifyDataSetChanged()
            }
        }

    }
}
