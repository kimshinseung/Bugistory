package com.anp56.bugistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.anp56.bugistory.databinding.ActivityOptionBinding

class OptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    
        //비밀번호 변경
        binding.passwordchange.setOnClickListener{
            val dialog=PasswordDialog(this)
            dialog.showDialog()
            dialog.setOnClickListener(object : PasswordDialog.OnDialogClickListener {
                override fun onClicked(pw: String)
                {

                    //비밀번호 변경 pw가 변경 값으로 들어옴
                    //textview.text = pw
                }

            })
        }

        //전화번호 변경
       c{
            val dialog=NumberDialog(this)
            dialog.showDialog()
            dialog.setOnClickListener(object : NumberDialog.OnDialogClickListener {
                override fun onClicked(phone: String)
                {
                    //전화번호 변경이 phone 값으로 들어옴
                    //textview.text = phone
                }

            })
        }

        binding.logout.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        
    }
}