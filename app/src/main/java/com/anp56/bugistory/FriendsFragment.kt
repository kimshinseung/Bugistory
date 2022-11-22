package com.anp56.bugistory

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.compose.ui.text.toLowerCase
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.anp56.bugistory.friend.Data
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
//import com.anp56.bugistory.friend.DataList
//import com.anp56.bugistory.friend.FriendsAdapter
import kotlinx.android.synthetic.main.fragment_friends.*
import kotlinx.android.synthetic.main.item_friend.view.*
import java.util.*
import kotlin.collections.ArrayList
data class Data (val profile :String, val name : String, val phonenumber : String, val email : String)
class FriendsFragment : Fragment() {
    private var itemlist: MutableList<Data> = mutableListOf()
    private var mAdapter: FriendsAdapter? = null
    var db : FirebaseFirestore = Firebase.firestore
    private val userdataCollectionRef = db.collection("userdata")




    inner class FriendsAdapter(
        private var DataList:MutableList<Data>
    ) : RecyclerView.Adapter<FriendsAdapter.CustomViewHolder>(), Filterable {

        var current: List<Data>? = null

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
            val data: Data = current!![position]
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

        }

        override fun getItemCount() = current!!.size

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val charString = constraint.toString()
                    if (charString.isNullOrBlank()) {
                        current = DataList
                    } else {
                        val filteredList = ArrayList<Data>()

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
                    current = filterResults?.values as ArrayList<Data>
                    notifyDataSetChanged()
                }
            }

        }
    }


    companion object{
        fun newInstance() : FriendsFragment {
            return FriendsFragment()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friends,container,false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val editText = view.findViewById<EditText>(R.id.search_friend)
        val friendsList = mutableListOf<Data>()
        userdataCollectionRef.get().addOnSuccessListener {
            for (data in it) {
                try {
                    val friendData = Data(
                        data.id,
                        data.get("name").toString(),
                        data.get("phone_number").toString(),
                        data.get("email").toString()
                    )
                    friendsList.add(friendData)
                } catch (_: Exception) {
                    Log.d("Update Post", "Post data parse failed.")
                }
            }
            itemlist = friendsList
            mAdapter = FriendsAdapter(itemlist as ArrayList<Data>)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView!!.adapter = mAdapter

            //recyclerView.adapter = FriendsAdapter(DataList)

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                    //recyclerView!!.adapter = mAdapter
                }

                override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //recyclerView!!.adapter = mAdapter
                }

                override fun afterTextChanged(charSequence: Editable?) {
                    mAdapter!!.filter.filter(charSequence)
                    //recyclerView!!.adapter = mAdapter
                }

            })
        }
        //recyclerView.adapter = FriendsAdapter(DataList)

//        editText.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                //recyclerView!!.adapter = mAdapter
//            }
//
//            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                //recyclerView!!.adapter = mAdapter
//            }
//
//            override fun afterTextChanged(charSequence: Editable?) {
//                mAdapter!!.filter.filter(charSequence)
//                //recyclerView!!.adapter = mAdapter
//            }
//
//        })
        val spaceDecoration = RecyclerDecoration(40)
        recyclerView.addItemDecoration(spaceDecoration)


        return view/*inflater.inflate(R.layout.fragment_friends, container, false)*/
    }


}