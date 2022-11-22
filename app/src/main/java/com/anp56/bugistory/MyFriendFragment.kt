package com.anp56.bugistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anp56.bugistory.databinding.FragmentMyFriendBinding
import com.anp56.bugistory.friend.FriendData
import com.anp56.bugistory.friend.FriendsAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_friends.*

class MyFriendFragment : Fragment() {
    lateinit var binding: FragmentMyFriendBinding
    var db : FirebaseFirestore = Firebase.firestore
    private val userdataCollectionRef = db.collection("userdata")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyFriendBinding.bind(view)
        val spaceDecoration = RecyclerDecoration(40)
        binding.recyclerView.addItemDecoration(spaceDecoration)
        createFriendList()
    }
    private fun createFriendList(){
        val friendsList = mutableListOf<FriendData>()
        userdataCollectionRef.document(Firebase.auth.uid.toString()).get()
            .addOnSuccessListener {
                var friendUidList = it.get("friends") as List<String>
                for (friendUid in friendUidList ){
                    userdataCollectionRef.document(friendUid).get().addOnSuccessListener { data ->
                        friendsList.add(
                            FriendData(
                                friendUid,
                                data.get("name").toString(),
                                data.get("phone_number").toString(),
                                data.get("email").toString()
                            )
                        )
                        binding.recyclerView.adapter = FriendsAdapter(friendsList)
                    }
                }
            }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        createFriendList()
    }
}