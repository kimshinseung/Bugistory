package com.anp56.bugistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.anp56.bugistory.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        println("Login activity activated.")
        binding.loginButton.setOnClickListener {
            doLogin(binding.idEditText.text.toString(),binding.passwordEditText.text.toString())
        }
    }
    fun doLogin(id: String, pass: String)
    {
        Firebase.auth.signInWithEmailAndPassword(id,pass)
            .addOnCompleteListener(this) {
                println("Login Task occur")
                if(it.isSuccessful){
                    startActivity(
                        Intent(this,MainActivity::class.java)
                    )
                    finish()
                }
                else{
                    Log.w("LoginActivity", "signInWithEmail", it.exception)
                    Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

    }
}