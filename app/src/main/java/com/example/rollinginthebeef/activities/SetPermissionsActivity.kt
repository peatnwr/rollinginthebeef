package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.databinding.ActivitySetPermissionsBinding
import com.example.rollinginthebeef.adapter.SetPermissionsAdapter
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.dataclass.UserPermission
import com.example.rollinginthebeef.retrofits.dataAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SetPermissionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetPermissionsBinding
    var userPermission = arrayListOf<UserPermission>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySetPermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        binding.btnBackToProfileAdminPage.setOnClickListener {
            val profilePage = Intent(this@SetPermissionsActivity, ProfileAdminActivity::class.java)
            profilePage.putExtra("adminData", adminData)
            startActivity(profilePage)
        }

        binding.btnToAddPermission.setOnClickListener {
            val addPermissionPage = Intent(this@SetPermissionsActivity, AddStaffActivity::class.java)
            addPermissionPage.putExtra("adminData", adminData)
            startActivity(addPermissionPage)
        }

        binding.recyclerViewPermissions.adapter = SetPermissionsAdapter(userPermission, adminData, applicationContext)
        binding.recyclerViewPermissions.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        callPermissionData()
    }

    fun callPermissionData(){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")
        userPermission.clear()
        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)
        api.retrieveUser().enqueue(object : Callback<List<UserPermission>> {
            override fun onResponse(call: Call<List<UserPermission>>, response: Response<List<UserPermission>>) {
                response.body()?.forEach {
                    userPermission.add(UserPermission(it.user_name, it.user_tel, it.user_email, it.user_address, it.user_type, it.user_id))
                }
                binding.recyclerViewPermissions.adapter = SetPermissionsAdapter(userPermission, adminData, applicationContext)
            }

            override fun onFailure(call: Call<List<UserPermission>>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }
}