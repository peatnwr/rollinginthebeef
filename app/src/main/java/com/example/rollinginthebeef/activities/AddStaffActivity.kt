package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityAddStaffBinding
import com.example.rollinginthebeef.dataclass.LoginUser
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.authenticationAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddStaffActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityAddStaffBinding
    var usertypeName = ""
    var userType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.user_type,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerUsertype.adapter = adapter
        binding.spinnerUsertype.onItemSelectedListener = this

        binding.btnBackToProfileAdminPage.setOnClickListener {
            val setPermissionPage = Intent(this@AddStaffActivity, SetPermissionsActivity::class.java)
            setPermissionPage.putExtra("adminData", adminData)
            startActivity(setPermissionPage)
        }

        binding.addStaff.setOnClickListener {
            val data = intent.extras
            val adminData: infoUserParcel? = data?.getParcelable("adminData")

            usertypeName.forEach {
                when(usertypeName){
                    "Admin" -> { userType = 1 }
                    "Delivery Man" -> { userType = 2 }
                }
            }

            Log.d("userType : ", userType.toString())

            val api: authenticationAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(authenticationAPI::class.java)
            if(binding.edtName.text.toString().trim().isNotEmpty() && binding.edtAddress.text.toString().trim().isNotEmpty() &&
                    binding.edtEmail.text.toString().trim().isNotEmpty() && binding.edtTel.text.toString().trim().isNotEmpty() &&
                    binding.edtUsername.text.toString().trim().isNotEmpty() && binding.edtPassword.text.toString().trim().isNotEmpty() &&
                    binding.edtCfPassword.text.toString().trim().isNotEmpty()){
                api.addStaff(
                    binding.edtName.text.toString(),
                    binding.edtEmail.text.toString(),
                    binding.edtTel.text.toString(),
                    binding.edtAddress.text.toString(),
                    binding.edtUsername.text.toString(),
                    userType,
                    binding.edtPassword.text.toString(),
                    binding.edtCfPassword.text.toString()
                ).enqueue(object : Callback<LoginUser> {
                    override fun onResponse(call: Call<LoginUser>, response: Response<LoginUser>) {
                        if(response.isSuccessful){
                            Toast.makeText(applicationContext, "Add Staff Successfull", Toast.LENGTH_SHORT).show()
                            val setPermissionsPage = Intent(this@AddStaffActivity, SetPermissionsActivity::class.java)
                            setPermissionsPage.putExtra("adminData", adminData)
                            startActivity(setPermissionsPage)
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
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val itemSpinner: String = parent?.getItemAtPosition(position).toString()
        usertypeName = itemSpinner
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}