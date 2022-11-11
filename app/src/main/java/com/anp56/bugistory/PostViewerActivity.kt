package com.anp56.bugistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.anp56.bugistory.databinding.ActivityPostViewerBinding

class PostViewerActivity : AppCompatActivity() {
    val binding by lazy { ActivityPostViewerBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Toast.makeText(this,"${currentPostIndex}의 글을 표시합니다.",Toast.LENGTH_SHORT).show()
        binding.returnButton.setOnClickListener {
            super.onBackPressed()
        }
    }
    companion object{
        var currentPostIndex = 0
    }
}