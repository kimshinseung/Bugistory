package com.anp56.bugistory

import android.app.Activity.RESULT_OK
import android.app.appsearch.AppSearchResult.RESULT_OK
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Gallery
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.lifecycle.ViewModelProvider
import com.anp56.bugistory.databinding.FragmentMainBinding
import com.anp56.bugistory.databinding.FragmentProfileBinding
import com.anp56.bugistory.post.PostAdapter
import com.anp56.bugistory.post.PostData
import com.anp56.bugistory.post.PostViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ProfileFragment : Fragment()
{
    lateinit var mainContext : Context
    lateinit var postViewModel : PostViewModel
    private val db: FirebaseFirestore = Firebase.firestore
    lateinit var storage:FirebaseStorage
    private val  postCollectionRef = db.collection("post")
    private val userCollectionRef = db.collection("userdata")
    private val userdataCollectionRef = db.collection("userdata")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage=Firebase.storage
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProfileBinding.bind(view)
        val adapter = PostAdapter(mainContext, mutableListOf())
        binding.postRecyclerView.adapter = adapter
        updatePostList()

        postViewModel.postData.observe(viewLifecycleOwner) {
            binding.postRecyclerView.adapter = PostAdapter(mainContext, it)
        }

        binding.swipeLayout.setOnRefreshListener {
            updatePostList()
            binding.swipeLayout.isRefreshing = false
        }

        userdataCollectionRef.document(Firebase.auth.uid.toString()).get()
            .addOnSuccessListener {
                binding.nameTextView.text = it.get("name").toString()
                binding.phoneTextView.text = it.get("phone_number").toString()
                binding.emailTextView.text = it.get("email").toString()
            }
            .addOnFailureListener {
                Toast.makeText(activity,"프로필 정보를 가져오지 못했습니다.",Toast.LENGTH_SHORT).show();
            }
        val imageRef=storage.getReferenceFromUrl("gs://bugistory.appspot.com/photo/${Firebase.auth.uid}.png")
        displayImageRef(imageRef,profile_background)
        binding.settingButton.setOnClickListener {
            startActivity(Intent(activity,OptionActivity::class.java))
        }
        binding.profileBackground.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, 1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1){
            var dataURI= data?.data
            //profile_background.setImageURI(dataURI).
            //Toast.makeText(activity,"프로필 사진을 가져왔습니다.",Toast.LENGTH_SHORT).show();
            var fileName="${Firebase.auth.uid}.png"
            if (dataURI != null) {
                storage.reference.child("photo").child(fileName)
                    .putFile(dataURI).addOnCompleteListener{
                        val imageRef=storage.getReferenceFromUrl("gs://bugistory.appspot.com/photo/${Firebase.auth.uid}.png")
                        displayImageRef(imageRef,profile_background)
                    }
            }

        }
    }
    private fun displayImageRef(imageRef: StorageReference?, view: ImageView) {
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
// Failed to download the image
        }
    }
    private fun updatePostList(){
        postCollectionRef.orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener {
            val formatter = SimpleDateFormat("YYYY년 MM월 dd일 HH시 mm분")
            val postList = mutableListOf<PostData>()
            for (data in it){
                try {
                    val postData = PostData(
                        data.id,
                        "알수없음",
                        formatter.format(Date(data.get("time").toString().toLong())),
                        data.get("content").toString(),
                        (data.get("like") as MutableList<String>),
                        (data.get("comment") as MutableList<Map<String,String>>)
                    )
                    userCollectionRef.document(data.get("uid").toString()).get()
                        .addOnSuccessListener { userdata ->
                            postData.username = userdata["name"].toString()
                            postList.add(postData)
                            postViewModel.setPostList(postList)
                        }
                        .addOnFailureListener {
                            postList.add(postData)
                            postViewModel.setPostList(postList)
                        }
                }
                catch (_ : Exception){
                    Log.d("Update Post","Post data parse failed.")
                }

            }
        }
            .addOnFailureListener {
                Toast.makeText(activity, "불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainContext = container!!.context;
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}