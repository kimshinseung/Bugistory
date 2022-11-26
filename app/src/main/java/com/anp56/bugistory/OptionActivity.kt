package com.anp56.bugistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.anp56.bugistory.data.ProfileDataViewModel
import com.anp56.bugistory.databinding.ActivityOptionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OptionActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val  userdataCollectionRef = db.collection("userdata")
    private lateinit var profileDataViewModel : ProfileDataViewModel
    private lateinit var binding: ActivityOptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileDataViewModel = ViewModelProvider(this).get(ProfileDataViewModel::class.java)
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
                    Toast.makeText(this,"이메일로 비밀번호 초기화 링크를 보냈습니다.", Toast.LENGTH_SHORT).show();
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
                    profileDataViewModel.updatePhoneNumber(applicationContext,phone)
                    profileDataViewModel.refreshData()
                }

            })
        }
        //로그아웃 버튼
        binding.logout.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            Firebase.auth.signOut()
            Toast.makeText(applicationContext,"로그아웃되었습니다.",Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
        //완료 버튼
        binding.completeButton.setOnClickListener {
            super.onBackPressed()
        }

    }
}