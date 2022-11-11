package com.anp56.bugistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.anp56.bugistory.databinding.ActivityOptionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OptionActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val  userdataCollectionRef = db.collection("userdata")
    private lateinit var binding: ActivityOptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기버튼
        binding.backButton.setOnClickListener{
            super.onBackPressed()
        }
        
        //비밀번호 변경
        binding.passwordchange.setOnClickListener{

            userdataCollectionRef.document(Firebase.auth.uid.toString()).get()
                .addOnSuccessListener {
                    val email= it.get("email").toString()
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                }
                .addOnFailureListener {
                    Toast.makeText(this,"이메일을 찾지 못했습니다.", Toast.LENGTH_SHORT).show();
                }

        }

        //전화번호 변경
       binding.numberchange.setOnClickListener{
            val dialog=NumberDialog(this)
            dialog.showDialog()
            dialog.setOnClickListener(object : NumberDialog.OnDialogClickListener {
                override fun onClicked(phone: String)
                {
                    userdataCollectionRef.document(Firebase.auth.uid.toString()).update("phone",phone)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext,"전화번호를 변경했습니다",Toast.LENGTH_SHORT).show()
                        }
                }

            })
        }
        //로그아웃 버튼
        binding.logout.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        //완료 버튼
        binding.completeButton.setOnClickListener {
            super.onBackPressed()
        }

    }
}