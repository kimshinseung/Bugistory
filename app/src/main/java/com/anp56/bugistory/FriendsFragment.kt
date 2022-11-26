package com.anp56.bugistory

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anp56.bugistory.friend.FriendData
import com.anp56.bugistory.friend.FriendsAdapter
import com.google.firebase.auth.ktx.auth
//import com.anp56.bugistory.friend.Data
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
//import com.anp56.bugistory.friend.DataList
//import com.anp56.bugistory.friend.FriendsAdapter
import kotlin.collections.ArrayList
class FriendsFragment : Fragment() {
    private var mAdapter: FriendsAdapter? = null
    var db : FirebaseFirestore = Firebase.firestore
    private val userdataCollectionRef = db.collection("userdata")

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
        val friendsList = mutableListOf<FriendData>()
        userdataCollectionRef.get().addOnSuccessListener {
            for (data in it) {
                try {
                    if (data.id == Firebase.auth.uid) continue
                    val friendData = FriendData(
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
            mAdapter = FriendsAdapter(friendsList)
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