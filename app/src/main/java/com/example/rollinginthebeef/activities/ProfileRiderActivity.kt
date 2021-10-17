package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityProfileRiderBinding
import com.example.rollinginthebeef.dataclass.infoUserParcel

class ProfileRiderActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileRiderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileRiderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val riderData: infoUserParcel? = data?.getParcelable("riderData")

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.selectedItemId = R.id.miUserRider
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.miDeliveryStatusRider -> {
                    val deliveryStatusRider = Intent(this@ProfileRiderActivity, MainRiderActivity::class.java)
                    deliveryStatusRider.putExtra("riderData", riderData)
                    startActivity(deliveryStatusRider)
                }
            }
            true
        }

        val edtName : EditText = binding.edtName
        val edtEmail : EditText = binding.edtEmail
        val edtTel : EditText = binding.edtTel
        val edtAddress : EditText = binding.edtAddress
        val edtUsername : EditText = binding.edtUsername
        val edtPosition : EditText = binding.edtPosition

        binding.textName.text = riderData?.nameUser
        edtName.setText(riderData?.nameUser)
        edtEmail.setText(riderData?.userEmail)
        edtTel.setText(riderData?.userTel)
        edtAddress.setText(riderData?.userAddress)
        edtUsername.setText(riderData?.userName)
        edtPosition.setText("Delivery Man")
        edtName.isEnabled = false
        edtEmail.isEnabled = false
        edtTel.isEnabled = false
        edtAddress.isEnabled = false
        edtUsername.isEnabled = false
        edtPosition.isEnabled = false
    }

    fun showOptionRider(v: View){
        val data = intent.extras
        val riderData: infoUserParcel? = data?.getParcelable("riderData")
        val popupMenu = PopupMenu(this@ProfileRiderActivity, binding.btnOptionRider)
        popupMenu.inflate(R.menu.menu_rider)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener{
            item: MenuItem? ->
            when(item!!.itemId){
                R.id.menuOrderHistoryRider -> {
                    val orderHistory = Intent(this@ProfileRiderActivity, OrderHistoryRiderActivity::class.java)
                    orderHistory.putExtra("riderData", riderData)
                    startActivity(orderHistory)
                }
            }
            true
        })
        popupMenu.show()
    }

    fun logoutFromRiderPage(v: View){
        val loginPage = Intent(this, LoginActivity::class.java)
        loginPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(loginPage)
        finish()
    }
}