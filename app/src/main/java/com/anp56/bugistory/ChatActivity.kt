package com.anp56.bugistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anp56.bugistory.chat.ChatAdapter
import com.anp56.bugistory.chat.ChatData
import com.anp56.bugistory.databinding.ActivityChatBinding
import com.anp56.bugistory.post.PostAdapter
import com.anp56.bugistory.post.PostData

class ChatActivity : AppCompatActivity() {
    val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val chats = mutableListOf<ChatData>()

        for (i in 1..10){
            println("Doing here")
            var count = i
            var cont = ""
            for (j in 0..count){
                cont += "글이 얼마나 적힐지 테스트"
            }
            chats.add(
                ChatData(
                    "유저 고유 아이디 번호가 들어간다",
                    if (i%2==0) "상대" else "나",
                    "이것이 채팅입니다. 이것이 채팅입니다. 이것이 채팅입니다. 이것이 채팅입니다.$count",
                    i %2 == 0
                )
            )
        }
        val adapter = ChatAdapter(applicationContext, chats)
        binding.chatRecyclerView.adapter = adapter
    }
}