package com.anp56.bugistory

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anp56.bugistory.databinding.ActivityLoginBinding
import com.anp56.bugistory.databinding.ActivityProfilefriendBinding
import com.anp56.bugistory.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.activity_profilefriend.*

class FriendProfileActivity : AppCompatActivity() {
    private val binding by lazy { ActivityProfilefriendBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val name = intent.getStringExtra("name")
        val phonenumber = intent.getStringExtra("phonenumber")
        val profile = intent.getIntExtra("profile",-1)
        binding.profileimage.setImageResource(profile)
        binding.phone.text = phonenumber
        binding.name.text = name


        binding.backButton.setOnClickListener{
            super.onBackPressed()
        }

        binding.chatButton.setOnClickListener {
        }

        //debug용 스킵 코드
        //startActivity(
        //    Intent(this,MainActivity::class.java)
        //)
        //finish()
        binding.friendButton.setOnClickListener {

        }
        binding.friendpostButton.setOnClickListener {

        }
    }
}