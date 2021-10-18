package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.rollinginthebeef.databinding.ActivityLoginBinding
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.LoginUser
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.authenticationAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest

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
            md5(binding.edtPassword.text.toString())
        ).enqueue(object : Callback<LoginUser> {
            override fun onResponse(call: Call<LoginUser>, response: Response<LoginUser>) {
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
                            mainActivity.putExtra("accountData", Account(binding.edtUsername.text.toString(), binding.edtPassword.text.toString()))
                            startActivity(mainActivity)
                        }
                        "1" -> {
                            val mainAdmin = Intent(this@LoginActivity, MainAdminActivity::class.java)
                            mainAdmin.putExtra("adminData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
                            startActivity(mainAdmin)
                        }
                        "2" -> {
                            val mainRider = Intent(this@LoginActivity, MainRiderActivity::class.java)
                            mainRider.putExtra("riderData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
                            startActivity(mainRider)
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "Username or password are incorrect!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginUser>, t: Throwable) {
                Toast.makeText(applicationContext, "Server isn't online now!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun md5(input: String) : String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun forgetPasswordPage(v: View){
        val forgetpwPage = Intent(this, ForgetPasswordActivity::class.java)
        forgetpwPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(forgetpwPage)
        finish()
    }

    fun registerPage(v: View){
        val regPage = Intent(this, RegisterActivity::class.java)
        regPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(regPage)
        finish()
    }

}