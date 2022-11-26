package com.postive.bugiwear.post
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.postive.bugiwear.R

class ImageCacheManager {
    companion object{
        private val storage = Firebase.storage
        private val cache : MutableMap<String,Bitmap> = hashMapOf()
        fun containImage(uid: String) : Boolean{
            return cache.containsKey(uid)
        }
        fun requestImage(uid : String,target : ImageView? = null) : Bitmap?{
            Log.d("Image requester",uid)
            synchronized(this){
                if (cache.containsKey(uid)){
                    target?.setImageBitmap(cache[uid])
                    return cache.get(uid)
                }
                else{
                    var bmp : Bitmap? = null
                    storage.reference.child("photo").child("$uid.png").getBytes(Long.MAX_VALUE).addOnSuccessListener {
                        bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                        cache[uid] = bmp!!
                        target?.setImageBitmap(bmp)
                    }
                        .addOnFailureListener {
                            Log.d("Image","Profile doesn't exist")
                            bmp = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.default_user_image)
                        }
                    return bmp
                }
            }
        }
        fun getLength() : Int{
            return cache.size
        }
        fun updateImage() {
            for (uid in cache.keys){
                requestImage(uid)
            }
        }
    }
}