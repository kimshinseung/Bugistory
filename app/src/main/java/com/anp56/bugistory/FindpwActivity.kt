package com.anp56.bugistory

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.anp56.bugistory.databinding.ActivityForgetpasswordBinding
import com.anp56.bugistory.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class FindpwActivity: AppCompatActivity()  {
    val binding by lazy { ActivityForgetpasswordBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val email = findViewById<EditText>(R.id.id_edit_text)

        binding.loginButton.setOnClickListener{
            FirebaseAuth.getInstance().sendPasswordResetEmail(email.text.toString())
        }
    }
}