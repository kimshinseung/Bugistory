package com.anp56.bugistory

import android.app.Activity.RESULT_OK
import android.app.appsearch.AppSearchResult.RESULT_OK
import android.content.ContentUris
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Gallery
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import com.anp56.bugistory.databinding.FragmentMainBinding
import com.anp56.bugistory.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import java.sql.Time

class ProfileFragment : Fragment()
{
    private val db: FirebaseFirestore = Firebase.firestore
    lateinit var storage:FirebaseStorage
    private val userdataCollectionRef = db.collection("userdata")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage=Firebase.storage


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentProfileBinding.bind(view)
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
            //profile_background.setImageURI(dataURI)
            Toast.makeText(activity,"프로필 사진을 가져왔습니다.",Toast.LENGTH_SHORT).show();
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}