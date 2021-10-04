package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityDeliveryStatusAdminBinding
import com.example.rollinginthebeef.modules.infoUserParcel

class DeliveryStatusAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeliveryStatusAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeliveryStatusAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        val userID = adminData?.userID.toString().toInt()
        val userName = adminData?.userName.toString()
        val userTel = adminData?.userTel.toString()
        val userAddress = adminData?.userAddress.toString()
        val userEmail = adminData?.userEmail.toString()
        val nameUser = adminData?.nameUser.toString()

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.selectedItemId = R.id.miDeliveryStatus
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miHomeAdmin -> {
                    val mainAdminPage = Intent(this@DeliveryStatusAdminActivity, MainAdminActivity::class.java)
                    mainAdminPage.putExtra("adminData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
                    startActivity(mainAdminPage)
                }
                R.id.miAddProduct -> {
                }
                R.id.miUserAdmin -> {
                    val profileAdminPage = Intent(this@DeliveryStatusAdminActivity, ProfileAdminActivity::class.java)
                    profileAdminPage.putExtra("adminData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
                    startActivity(profileAdminPage)
                }
            }
            true
        }
    }
}