package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityOrderAdminBinding
import com.example.rollinginthebeef.modules.infoUserParcel

class OrderAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderAdminBinding.inflate(layoutInflater)
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
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miHomeAdmin -> {
                    val mainAdminPage = Intent(this@OrderAdminActivity, MainAdminActivity::class.java)
                    mainAdminPage.putExtra("adminData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
                    startActivity(mainAdminPage)
                }
                R.id.miDeliveryStatus -> {
                    val deliveryStatusAdminPage = Intent(this@OrderAdminActivity, DeliveryStatusAdminActivity::class.java)
                    deliveryStatusAdminPage.putExtra("adminData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
                    startActivity(deliveryStatusAdminPage)
                }
                R.id.miAddProduct -> {
                }
                R.id.miUserAdmin -> {
                    val profileAdminPage = Intent(this@OrderAdminActivity, ProfileAdminActivity::class.java)
                    profileAdminPage.putExtra("adminData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
                    startActivity(profileAdminPage)
                }
            }
            true
        }
    }
}