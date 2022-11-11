package com.anp56.bugistory

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anp56.bugistory.databinding.ActivityPostBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {
    private var isSeeOnlyMe = true
    private val db: FirebaseFirestore= Firebase.firestore
    private val  postCollectionRef = db.collection("post")
    private lateinit var binding: ActivityPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기 버튼
        binding.backButton.setOnClickListener{
            super.onBackPressed()
        }

        //전체공개, 나만 보기
        binding.allopen.setOnCheckedChangeListener{ _,isChecked->
            isSeeOnlyMe = !isChecked
        }
        //완료 버튼
        binding.completeButton.setOnClickListener{
            val content= binding.content.text.toString()
            val uid=Firebase.auth.uid
            val time= System.currentTimeMillis()
            val like= mutableListOf<String>()
            val comment= hashMapOf<String,String>()
            val see= isSeeOnlyMe
            val postinf= hashMapOf(
                "content" to content,
                "uid" to uid,
                "time" to time,
                "like" to like,
                "comment" to comment,
                "see" to see
            )
            postCollectionRef.document().set(postinf).addOnFailureListener{
                Toast.makeText(this,"게시물 작성에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
            super.onBackPressed()
        }
        
        

    }

}