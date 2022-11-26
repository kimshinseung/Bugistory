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
import com.anp56.bugistory.data.ProfileDataViewModel
import com.anp56.bugistory.databinding.FragmentMainBinding
import com.anp56.bugistory.databinding.FragmentProfileBinding
import com.anp56.bugistory.post.PostAdapter
import com.anp56.bugistory.post.PostData
import com.anp56.bugistory.post.PostViewModel
import com.anp56.bugistory.tools.ImageCacheManager
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
    lateinit var profileDataViewModel: ProfileDataViewModel
    private val db: FirebaseFirestore = Firebase.firestore
    private val storage = Firebase.storage
    private val  postCollectionRef = db.collection("post")
    private val userdataCollectionRef = db.collection("userdata")
    lateinit var binding : FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        profileDataViewModel = ViewModelProvider(this).get(ProfileDataViewModel::class.java)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        val adapter = PostAdapter(mainContext, mutableListOf())
        binding.postRecyclerView.adapter = adapter
        updatePostList()

        postViewModel.postData.observe(viewLifecycleOwner) {
            binding.postRecyclerView.adapter = PostAdapter(mainContext, it)
        }
        profileDataViewModel.username.observe(viewLifecycleOwner){
            binding.nameTextView.text = it
        }
        profileDataViewModel.email.observe(viewLifecycleOwner){
            binding.emailTextView.text = it
        }
        profileDataViewModel.phoneNumber.observe(viewLifecycleOwner){
            binding.phoneTextView.text = it
        }
        profileDataViewModel.refreshData()

        binding.swipeLayout.setOnRefreshListener {
            updatePostList()
            binding.swipeLayout.isRefreshing = false
        }

        binding.profileImage.setImageBitmap(ImageCacheManager.requestImage(Firebase.auth.uid.toString()))

        binding.settingButton.setOnClickListener {
            startActivity(Intent(activity,OptionActivity::class.java))
        }

        binding.profileImage.setOnClickListener{
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
                        binding.profileImage.setImageBitmap(ImageCacheManager.requestImage(Firebase.auth.uid.toString()))
                    }
            }

        }
    }
    private fun updatePostList(){
        postCollectionRef.whereEqualTo("uid",Firebase.auth.uid).get().addOnSuccessListener {
            val postList = mutableListOf<PostData>()
            for (data in it){
                try {
                    val postData = PostData(
                        data.id,
                        data.get("uid").toString(),
                        "알 수 없음",
                        data.get("time").toString(),
                        data.get("content").toString(),
                        (data.get("like") as MutableList<String>),
                        (data.get("comment") as MutableList<Map<String,String>>)
                    )
                    userdataCollectionRef.document(data.get("uid").toString()).get()
                        .addOnSuccessListener { userdata ->
                            postData.username = userdata["name"].toString()
                            postList.add(postData)
                            postList.sortByDescending { it -> it.date }
                            postViewModel.setPostList(postList)
                        }
                        .addOnFailureListener {
                            postList.add(postData)
                            postList.sortByDescending { it -> it.date }
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
                it.printStackTrace()
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