package com.anp56.bugistory

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.anp56.bugistory.chat.ChatAdapter
import com.anp56.bugistory.chat.ChatData
import com.anp56.bugistory.comment.CommentAdapter
import com.anp56.bugistory.databinding.ActivityPostViewerBinding
import com.anp56.bugistory.post.PostAdapter
import com.anp56.bugistory.post.PostData
import com.anp56.bugistory.post.PostViewModel
import com.anp56.bugistory.tools.ImageCacheManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PostViewerActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val storage = Firebase.storage
    private val  postCollectionRef = db.collection("post")
    private val userdataCollectionRef = db.collection("userdata")
    private val commentData = MutableLiveData<List<ChatData>>()
    private val commentDataBase = mutableListOf<ChatData>()
    val binding by lazy { ActivityPostViewerBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = ChatAdapter(this, mutableListOf())
        binding.commentRecyclerView.adapter = adapter
        commentData.observe(this) {
            val adapter = ChatAdapter(this, it)
            binding.commentRecyclerView.adapter = adapter
        }
        val formatter = SimpleDateFormat("YYYY년 MM월 dd일 HH시 mm분")
        //Toast.makeText(this,"${currentPostIndex}의 글을 표시합니다.",Toast.LENGTH_SHORT).show()
        binding.postContent.text = currentPost.content
        binding.userName.text = currentPost.username
        binding.dateText.text = formatter.format(currentPost.date.toLong())
        binding.likeCount.text = currentPost.like.size.toString()
        ImageCacheManager.requestImage(currentPost.uid,binding.userImage)
        binding.likeButton.setOnClickListener {
            val uid = Firebase.auth.uid.toString()
            if (currentPost.like.contains(uid)){
                Toast.makeText(this,"이미 좋아요하신 글 입니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            currentPost.like.add(uid)
            postCollectionRef.document(currentPost.id).update("like",currentPost.like)
                .addOnSuccessListener {
                    Toast.makeText(this,"이 글을 좋아요 하셨습니다.",Toast.LENGTH_SHORT).show()
                    binding.likeCount.text = currentPost.like.size.toString()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"좋아요에 실패했습니다.",Toast.LENGTH_SHORT).show()
                    currentPost.like.remove(uid)
                    binding.likeCount.text = currentPost.like.size.toString()
                }
        }

        binding.createComment.setOnClickListener {
            if (binding.commentContent.text.isBlank()){
                Toast.makeText(this,"입력하지 않으셨습니다",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val comment = hashMapOf<String,String>()
            comment["uid"] = Firebase.auth.uid.toString()
            comment["comment"] = binding.commentContent.text.toString()
            currentPost.comment.add(comment)

            postCollectionRef.document(currentPost.id).update("comment", currentPost.comment)
                .addOnSuccessListener {
                    Toast.makeText(this,"댓글을 작성하셨습니다.",Toast.LENGTH_SHORT).show()
                    binding.commentContent.text.clear()
                    //댓글창 업데이트 코드
                    updateCommentList()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"댓글 작성에 실패했습니다.",Toast.LENGTH_SHORT).show()
                    currentPost.comment.remove(comment)
                }
        }

        binding.returnButton.setOnClickListener {
            super.onBackPressed()
        }

        updateCommentList()
    }
    private fun updateCommentList(){
        commentDataBase.clear()
        //Toast.makeText(this,"${currentPost.comment[0].size}",Toast.LENGTH_SHORT).show()
        for (data in currentPost.comment){
            val currentUid = data["uid"]
            userdataCollectionRef.document(currentUid!!).get()
                .addOnSuccessListener { userdata ->
                    var comment = ChatData(
                        currentUid,
                        userdata["name"].toString(),
                        data["comment"].toString(),
                        true
                    )
                    commentDataBase.add(comment)
                    commentData.value = commentDataBase
                }
        }
    }
    companion object{
        lateinit var currentPost : PostData
    }
}