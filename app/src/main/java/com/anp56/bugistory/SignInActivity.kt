package com.anp56.bugistory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anp56.bugistory.databinding.ActivitySigninBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class SignInActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val userdataCollectionRef = db.collection("userdata")

    private val binding by lazy { ActivitySigninBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.signinButton.setOnClickListener {
            Log.d("Doing login", "test" );
            if (Pattern.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\$",binding.emailEditText.text.toString())){
                Toast.makeText(this,"올바른 이메일 형태가 아닙니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (Pattern.matches("^\\d{3}-\\d{3,4}-\\d{4}\$",binding.phoneEditText.text.toString())){
                Toast.makeText(this,"올바른 전화번호 형태가 아닙니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (binding.passwordConfirmEditText.text.toString() != binding.passwordEditText.text.toString()){
                Toast.makeText(this,"비밀번호가 서로 동일 하지 않습니다. 다시 확인해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Firebase.auth.createUserWithEmailAndPassword(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d("Sign in task","회원가입 성공");
                        val user = Firebase.auth.currentUser
                        val email = user!!.email
                        val uid = user.uid
                        val name = binding.nameEditText.text.toString()
                        val phoneNumber = binding.phoneEditText.text.toString()
                        val userdata = hashMapOf(
                            "name" to name,
                            "phone_number" to phoneNumber,
                            "email" to email
                        )
                        userdataCollectionRef.document(uid).set(userdata).addOnFailureListener {
                            Toast.makeText(this,"유저 데이터를 초기화하는데 실패했습니다.",Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Log.d("Sign in task","회원가입 실패");
                        Toast.makeText(this,"회원가입에 실패하셨습니다.",Toast.LENGTH_SHORT).show()
                    }
                    startActivity(
                        Intent(this,LoginActivity::class.java)
                    )
                    finish()
                }
        }
    }
}