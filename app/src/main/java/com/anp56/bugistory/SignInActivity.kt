package com.anp56.bugistory

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anp56.bugistory.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class SignInActivity : AppCompatActivity() {
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
            if (Pattern.matches("^01(?:0|1|[6-9]) - (?:\\d{3}|\\d{4}) - \\d{4}\$",binding.phoneEditText.text.toString())){
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
                        val hashMap: HashMap<Any, String?> = HashMap()
                        hashMap["uid"] = uid
                        hashMap["email"] = email
                        hashMap["name"] = name
                        hashMap["phone_number"] = phoneNumber
                        val reference  =FirebaseDatabase.getInstance().getReference("Users")
                        reference.child(uid).setValue(hashMap)
                    }
                    else{
                        Log.d("Sign in task","회원가입 실패");
                        Toast.makeText(this,"회원가입에 실패하셨습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}