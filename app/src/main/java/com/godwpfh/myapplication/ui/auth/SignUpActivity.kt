package com.godwpfh.myapplication.ui.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.godwpfh.myapplication.data.remote.request.RequestSignUp
import com.godwpfh.myapplication.data.remote.response.ResponseSignUp
import com.godwpfh.myapplication.data.remote.ServiceCreator
import com.godwpfh.myapplication.data.remote.response.ResponseSignIn
import com.godwpfh.myapplication.databinding.ActivitySignUpBinding
import com.godwpfh.myapplication.util.enqueueUtil
import com.godwpfh.myapplication.util.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        finishClicked()
    }

    private fun finishClicked(){
        binding.buttonFinishSignup.setOnClickListener {
            if (binding.edittextSignupName.text.isEmpty() || binding.edittextSignupId.text.isEmpty() || binding.edittextSignupPw.text.isEmpty()) {
                Toast.makeText(this, "입력되지 않은 정보가 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signUpNetwork()

        }
    }


    private fun signUpNetwork(){
        val requestSignUp = RequestSignUp(
            name=binding.edittextSignupName.text.toString(),
            email=binding.edittextSignupId.text.toString(),
            password = binding.edittextSignupPw.text.toString()
        )
        val call: Call<ResponseSignUp> = ServiceCreator.soptService.postSignup(requestSignUp)

        call.enqueueUtil(
            onSuccess = {
                showToast("회원가입에 성공했습니다")
                moveToSignIn()
            },
            onError = {
                showToast("회원가입에 실패")
            }
        )

//        call.enqueue(object: Callback<ResponseSignUp> {
//            override fun onResponse(
//                call: Call<ResponseSignUp>,
//                response: Response<ResponseSignUp>
//            ) {
//                if(response.isSuccessful){
//                    //val data=response.body()?.data
//                    Log.d(TAG,"SignUpActivity, response.body: ${response.body()}")
//                    Toast.makeText(this@SignUpActivity, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show()
//                    moveToSignIn()
//                    //다음 페이지로 이동
//                }else if(response.code()==409){
//                    //중복된 경우
//                    Toast.makeText(this@SignUpActivity, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
//                }else
//
//                    Toast.makeText(this@SignUpActivity, "회원가입에 실패하였습니다.",Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onFailure(call: Call<ResponseSignUp>, t: Throwable) {
//                Log.d("NetworkTest","SignUpActivity - onFailure() called, error:$t ")
//            }
//
//        })

    }

    private fun moveToSignIn(){
        val intent = Intent(this, SignInActivity::class.java).apply{
            putExtra("id", binding.edittextSignupId.text.toString())
            putExtra("pw", binding.edittextSignupPw.text.toString())
        }
        setResult(RESULT_OK, intent)

        finish()
    }
}