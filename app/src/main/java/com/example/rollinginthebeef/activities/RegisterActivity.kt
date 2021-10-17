package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.rollinginthebeef.databinding.ActivityRegisterBinding
import com.example.rollinginthebeef.dataclass.RegisterUser
import com.example.rollinginthebeef.retrofits.authenticationAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun register(v: View){
        val api: authenticationAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(authenticationAPI::class.java)
        api.registerUser(
            binding.edtUsernameReg.text.toString(),
            md5(binding.edtPasswordReg.text.toString()),
            binding.edtTelReg.text.toString(),
            binding.edtAddReg.text.toString(),
            binding.edtEmailReg.text.toString(),
            binding.edtNameReg.text.toString()
        ).enqueue(object : Callback<RegisterUser> {
            override fun onResponse(call: Call<RegisterUser>, response: Response<RegisterUser>) {
                if(response.isSuccessful()){
                    Toast.makeText(applicationContext, "Register Successfully", Toast.LENGTH_SHORT).show()
                    val loginPage = Intent(this@RegisterActivity, LoginActivity::class.java)
                    loginPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(loginPage)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "User already exist!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterUser>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    fun md5(input: String) : String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun backToLogin(v: View){
        val loginPage = Intent(this, LoginActivity::class.java)
        loginPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(loginPage)
        finish()
    }
}