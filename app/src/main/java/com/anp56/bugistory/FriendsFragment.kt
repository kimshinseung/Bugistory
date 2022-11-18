package com.anp56.bugistory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anp56.bugistory.friend.DataList
import com.anp56.bugistory.friend.FriendsAdapter

class FriendsFragment : Fragment() {
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
        editText.addTextChangedListener()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = FriendsAdapter(DataList)



        val spaceDecoration = RecyclerDecoration(40)
        recyclerView.addItemDecoration(spaceDecoration)


        return view/*inflater.inflate(R.layout.fragment_friends, container, false)*/
    }
}