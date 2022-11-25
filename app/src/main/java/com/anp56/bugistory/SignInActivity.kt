package com.anp56.bugistory

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anp56.bugistory.databinding.ActivitySigninBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.regex.Pattern


class SignInActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val userdataCollectionRef = db.collection("userdata")
    private val storage = Firebase.storage
    private val binding by lazy { ActivitySigninBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.signinButton.setOnClickListener {
            if (binding.emailEditText.text.isBlank() || binding.nameEditText.text.isBlank() ||
                binding.passwordEditText.text.isBlank() || binding.passwordConfirmEditText.text.isBlank()){
                Toast.makeText(this,"빈칸이 남아있습니다. 빈칸을 모두 채워주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text).matches()){
                Toast.makeText(this,"올바른 이메일 형태가 아닙니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", binding.phoneEditText.text)){
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
                        val userFriendsList = mutableListOf<String>()
                        val userdata = hashMapOf(
                            "name" to name,
                            "phone_number" to phoneNumber,
                            "email" to email,
                            "friends" to userFriendsList
                        )
                        userdataCollectionRef.document(uid).set(userdata).addOnFailureListener {
                            Toast.makeText(this,"유저 데이터를 초기화하는데 실패했습니다.",Toast.LENGTH_SHORT).show()
                        }
                        var fileName="${Firebase.auth.uid}.png"
                        //storage.reference.child("photo").child(fileName).putFile(Bitmap.)
                    }
                    else{
                        Toast.makeText(this,"회원가입에 실패하셨습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}