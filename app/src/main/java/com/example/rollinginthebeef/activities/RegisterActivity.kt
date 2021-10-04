package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.rollinginthebeef.databinding.ActivityRegisterBinding
import com.example.rollinginthebeef.modules.registerUser
import com.example.rollinginthebeef.retrofits.authenticationAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            binding.edtPasswordReg.text.toString(),
            binding.edtTelReg.text.toString(),
            binding.edtAddReg.text.toString(),
            binding.edtEmailReg.text.toString(),
            binding.edtNameReg.text.toString()
        ).enqueue(object : Callback<registerUser> {
            override fun onResponse(call: Call<registerUser>, response: Response<registerUser>) {
                if(response.isSuccessful()){
                    Toast.makeText(applicationContext, "Register Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "User already exist!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<registerUser>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    fun backToLogin(v: View){
        val loginPage = Intent(this, LoginActivity::class.java)
        startActivity(loginPage)
    }
}