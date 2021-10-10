package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityEditProfileAdminBinding
import com.example.rollinginthebeef.modules.infoUserParcel

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
        edtPassword.setText("")
        edtCfPassword.setText("")
    }

    fun confirmEditAdminProfile(v: View){

    }

    fun backToProfileAdmin(v: View){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        val userID = adminData?.userID.toString().toInt()
        val userName = adminData?.userName.toString()
        val userTel = adminData?.userTel.toString()
        val userAddress = adminData?.userAddress.toString()
        val userEmail = adminData?.userEmail.toString()
        val nameUser = adminData?.nameUser.toString()

        val profileAdmin = Intent(this, ProfileAdminActivity::class.java)
        profileAdmin.putExtra("adminData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
        startActivity(profileAdmin)
    }

}