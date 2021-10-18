package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityEditProfileAdminBinding
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

class EditProfileAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        val edtNameUser: EditText = findViewById(R.id.edtNameEditAdmin)
        val edtEmail: EditText = findViewById(R.id.edtEmailEditAdmin)
        val edtTel: EditText = findViewById(R.id.edtTelEditAdmin)
        val edtAddress: EditText = findViewById(R.id.edtAddressEditAdmin)
        val edtUsername: EditText = findViewById(R.id.edtUsernameEditAdmin)
        val edtPassword: EditText = findViewById(R.id.edtPasswordEditAdmin)
        val edtCfPassword: EditText = findViewById(R.id.edtCfPwEditAdmin)

        binding.textName.text = adminData?.nameUser
        edtNameUser.setText(adminData?.nameUser)
        edtEmail.setText(adminData?.userEmail)
        edtTel.setText(adminData?.userTel)
        edtAddress.setText(adminData?.userAddress)
        edtUsername.setText(adminData?.userName)
        edtUsername.isEnabled = false
        edtPassword.setText("")
        edtCfPassword.setText("")
    }

    fun confirmEditAdminProfile(v: View){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")
        val api: authenticationAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(authenticationAPI::class.java)
        if(binding.edtNameEditAdmin.text.toString().trim().isNotEmpty() && binding.edtEmailEditAdmin.text.toString().trim().isNotEmpty() &&
                binding.edtTelEditAdmin.text.toString().trim().isNotEmpty() && binding.edtAddressEditAdmin.text.toString().trim().isNotEmpty() &&
                binding.edtUsernameEditAdmin.text.toString().trim().isNotEmpty() && binding.edtPasswordEditAdmin.text.toString().trim().isNotEmpty() &&
                binding.edtCfPwEditAdmin.text.toString().trim().isNotEmpty()){
            api.editProfileAdmin(
                adminData?.userID.toString().toInt(),
                binding.edtNameEditAdmin.text.toString(),
                binding.edtEmailEditAdmin.text.toString(),
                binding.edtTelEditAdmin.text.toString(),
                binding.edtAddressEditAdmin.text.toString(),
                binding.edtUsernameEditAdmin.text.toString(),
                md5(binding.edtPasswordEditAdmin.text.toString()),
                md5(binding.edtCfPwEditAdmin.text.toString())
            ).enqueue(object : Callback<LoginUser> {
                override fun onResponse(call: Call<LoginUser>, response: Response<LoginUser>) {
                    if(response.isSuccessful){
                        Toast.makeText(applicationContext, "Edited information Successful", Toast.LENGTH_SHORT).show()
                        val loginPage = Intent(this@EditProfileAdminActivity, LoginActivity::class.java)
                        loginPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(loginPage)
                        finish()
                    }
                }

                override fun onFailure(call: Call<LoginUser>, t: Throwable) {
                    return t.printStackTrace()
                }
            })
        } else {
            Toast.makeText(applicationContext, "Please complete information.", Toast.LENGTH_SHORT).show()
        }
    }

    fun md5(input: String) : String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun backToProfileAdmin(v: View){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        val profileAdmin = Intent(this, ProfileAdminActivity::class.java)
        profileAdmin.putExtra("adminData", adminData)
        startActivity(profileAdmin)
    }
}