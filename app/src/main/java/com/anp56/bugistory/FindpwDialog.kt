package com.anp56.bugistory

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.rpc.context.AttributeContext

class FindpwDialog(context: Context)
{
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }
    
    
    fun showDialog()
    {
        dialog.setContentView(R.layout.findpw_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val remake = dialog.findViewById<EditText>(R.id.pw_make)


        fun findPassword(){ //비밀번호 재설정 함수
            FirebaseAuth.getInstance().sendPasswordResetEmail(remake.text.toString())
                .addOnCompleteListener{ task->
                    if(task.isSuccessful){
                        dialog.dismiss()

                    }else{
                        dialog.dismiss()
                    }
                }
        }
        
        
        dialog.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.finish_button).setOnClickListener {
            findPassword() // 비밀번호 재설정 메일 보냄.
            
        }

    }

    interface OnDialogClickListener
    {
        fun onClicked(remake: String)
    }

}