package com.anp56.bugistory

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.anp56.bugistory.databinding.FragmentMainBinding
import com.anp56.bugistory.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import java.sql.Time

class ProfileFragment : Fragment()
{
    private val db: FirebaseFirestore = Firebase.firestore
    private val userdataCollectionRef = db.collection("userdata")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProfileBinding.bind(view)
        userdataCollectionRef.document(Firebase.auth.uid.toString()).get()
            .addOnSuccessListener {
                binding.nameTextView.text = it.get("name").toString()
                binding.phoneTextView.text = it.get("phone_number").toString()
                binding.emailTextView.text = it.get("email").toString()
            }
            .addOnFailureListener {
                Toast.makeText(activity,"프로필 정보를 가져오지 못했습니다.",Toast.LENGTH_SHORT).show();
            }
        binding.settingButton.setOnClickListener {
            startActivity(Intent(activity,OptionActivity::class.java))
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}