package com.postive.bugiwear

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.postive.bugiwear.databinding.ActivitySettingBinding

class SettingActivity : Activity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logoutButton.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            Firebase.auth.signOut()
            Toast.makeText(applicationContext,"로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
    }
}