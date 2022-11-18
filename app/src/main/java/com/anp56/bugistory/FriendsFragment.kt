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
import android.widget.Adapter
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.compose.ui.text.toLowerCase
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anp56.bugistory.friend.Data

import com.anp56.bugistory.friend.FriendsAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_friends.*
import java.util.*
import kotlin.collections.ArrayList

class FriendsFragment : Fragment() {
    var db : FirebaseFirestore = Firebase.firestore
    private val userdataCollectionRef = db.collection("userdata")

    private var itemlist: MutableList<Data> = mutableListOf()
    private var mAdapter: FriendsAdapter? = null

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
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_friends,container,false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val editText = view.findViewById<EditText>(R.id.search_friend)
        mAdapter = FriendsAdapter((itemlist as ArrayList<Data>))
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView!!.adapter = mAdapter
        val friendsList = mutableListOf<Data>()
        userdataCollectionRef.get()?.addOnSuccessListener {
            for (data in it) {
                try {
                    val friendData = Data(
                        data.id,
                        data.get("name").toString(),
                        data.get("phone_number").toString(),
                        data.get("email").toString()
                    )
                    var item = friendData
                    friendsList.add(item)
                } catch (_: Exception) {
                    Log.d("Update Post", "Post data parse failed.")
                }
            }
            recyclerView.adapter = FriendsAdapter(friendsList)
        }


        //recyclerView.adapter = FriendsAdapter(DataList)

        editText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
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





        val spaceDecoration = RecyclerDecoration(40)
        recyclerView.addItemDecoration(spaceDecoration)


        return view/*inflater.inflate(R.layout.fragment_friends, container, false)*/
    }


}