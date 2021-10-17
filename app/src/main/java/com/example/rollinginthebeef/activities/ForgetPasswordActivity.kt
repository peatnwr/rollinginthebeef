package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.rollinginthebeef.databinding.ActivityForgetPasswordBinding
import com.example.rollinginthebeef.dataclass.changePasswordUser
import com.example.rollinginthebeef.retrofits.authenticationAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun forgetPassword(v: View){
        val api: authenticationAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(authenticationAPI::class.java)
        api.changePasswordUser(
            binding.edtUsernameForgetPassword.text.toString(),
            md5(binding.edtPasswordForgetPassword.text.toString()),
            md5(binding.edtCfForgetPassword.text.toString())
        ).enqueue(object : Callback<changePasswordUser> {
            override fun onResponse(call: Call<changePasswordUser>, response: Response<changePasswordUser>) {
                if(response.isSuccessful()){
                    Toast.makeText(applicationContext, "Change Password Success", Toast.LENGTH_SHORT).show()
                    val loginPage = Intent(this@ForgetPasswordActivity, LoginActivity::class.java)
                    loginPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(loginPage)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Change Password Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<changePasswordUser>, t: Throwable) {
                Toast.makeText(applicationContext, "Password does not match", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun md5(input: String) : String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun backToLogin(v: View){
        val loginPage = Intent(this@ForgetPasswordActivity, LoginActivity::class.java)
        loginPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(loginPage)
        finish()
    }
}