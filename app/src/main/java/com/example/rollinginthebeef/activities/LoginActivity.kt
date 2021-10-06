package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.rollinginthebeef.databinding.ActivityLoginBinding
import com.example.rollinginthebeef.modules.infoUserParcel
import com.example.rollinginthebeef.modules.loginUser
import com.example.rollinginthebeef.retrofits.authenticationAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun login(v: View){
        val api: authenticationAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(authenticationAPI::class.java)
        api.loginUser(
            binding.edtUsername.text.toString(),
            binding.edtPassword.text.toString()
        ).enqueue(object : Callback<loginUser> {
            override fun onResponse(call: Call<loginUser>, response: Response<loginUser>) {
                if(response.isSuccessful){
                    val userID = response.body()?.user_id.toString().toInt()
                    val userName = response.body()?.user_username.toString()
                    val userTel = response.body()?.user_tel.toString()
                    val userAddress = response.body()?.user_address.toString()
                    val userEmail = response.body()?.user_email.toString()
                    val nameUser = response.body()?.user_name.toString()
                    when(response.body()?.user_type.toString()){
                        "0" -> {
                            val mainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                            mainActivity.putExtra("userData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
                            startActivity(mainActivity)
                        }
                        "1" -> {
                            val mainAdmin = Intent(this@LoginActivity, MainAdminActivity::class.java)
                            mainAdmin.putExtra("adminData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
                            startActivity(mainAdmin)
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "Username or password are incorrect!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<loginUser>, t: Throwable) {
                Toast.makeText(applicationContext, "onFailure Alert!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun forgetPasswordPage(v: View){
        val forgetpwPage = Intent(this, ForgetPasswordActivity::class.java)
        startActivity(forgetpwPage)
    }

    fun registerPage(v: View){
        val regPage = Intent(this, RegisterActivity::class.java)
        startActivity(regPage)
    }

}