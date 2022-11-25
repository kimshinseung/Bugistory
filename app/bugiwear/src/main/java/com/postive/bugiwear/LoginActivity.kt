package com.postive.bugiwear

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.postive.bugiwear.databinding.ActivityLoginBinding

class LoginActivity : Activity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Firebase.auth.currentUser != null){
            //Toast.makeText(this,"Current user is ${Firebase.auth.uid}",Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this,MainActivity::class.java)
            )
            finish()
        }
        binding.loginButton.setOnClickListener {
            doLogin(binding.emailEditText.text.toString(),binding.passwordEditText.text.toString())
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