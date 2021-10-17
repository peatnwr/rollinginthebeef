package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityProfileAdminBinding
import com.example.rollinginthebeef.dataclass.infoUserParcel

class ProfileAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.selectedItemId = R.id.miUserAdmin
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miHomeAdmin -> {
                    val mainAdminPage = Intent(this@ProfileAdminActivity, MainAdminActivity::class.java)
                    mainAdminPage.putExtra("adminData", adminData)
                    startActivity(mainAdminPage)
                }
                R.id.miDeliveryStatus -> {
                    val deliveryStatusAdminPage = Intent(this@ProfileAdminActivity, DeliveryStatusAdminActivity::class.java)
                    deliveryStatusAdminPage.putExtra("adminData", adminData)
                    startActivity(deliveryStatusAdminPage)
                }
                R.id.miAddProduct -> {
                    val addProductAdminPage = Intent(this@ProfileAdminActivity, AddProductAdminActivity::class.java)
                    addProductAdminPage.putExtra("adminData", adminData)
                    startActivity(addProductAdminPage)
                }
            }
            true
        }

        binding.orderAdmin.setOnClickListener {
            val orderAdmin = Intent(this@ProfileAdminActivity, OrderAdminActivity::class.java)
            orderAdmin.putExtra("adminData", adminData)
            startActivity(orderAdmin)
        }

        val edtNameText: EditText = findViewById(R.id.edtNameAdminPage)
        val edtEmailText: EditText = findViewById(R.id.edtEmailAdminPage)
        val edtTelText: EditText = findViewById(R.id.edtTelAdminPage)
        val edtAddressText: EditText = findViewById(R.id.edtAddressAdminPage)
        val edtUsernameText: EditText = findViewById(R.id.edtUsernameAdminPage)

        binding.textUsername.text = adminData?.nameUser
        edtNameText.setText(adminData?.nameUser)
        edtEmailText.setText(adminData?.userEmail)
        edtTelText.setText(adminData?.userTel)
        edtAddressText.setText(adminData?.userAddress)
        edtUsernameText.setText(adminData?.userName)
        edtNameText.isEnabled = false
        edtEmailText.isEnabled = false
        edtTelText.isEnabled = false
        edtAddressText.isEnabled = false
        edtUsernameText.isEnabled = false

    }

    fun showOptionAdminPage(v: View){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")
        val popupMenu = PopupMenu(this, binding.btnOptionAdminPage)
        popupMenu.inflate(R.menu.menu_admin)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
            item: MenuItem? ->
            when(item!!.itemId){
                R.id.menuSetPermission -> {
                    val setPermissionsPage = Intent(this, SetPermissionsActivity::class.java)
                    setPermissionsPage.putExtra("adminData", adminData)
                    startActivity(setPermissionsPage)
                }
                R.id.menuCustomerAccount -> {
                    val customerAccountPage = Intent(this, CustomerAccountActivity::class.java)
                    customerAccountPage.putExtra("adminData", adminData)
                    startActivity(customerAccountPage)
                }
                R.id.menuOrderHistory -> {
                    val orderHistoryPage = Intent(this, OrderHistoryAdminActivity::class.java)
                    orderHistoryPage.putExtra("adminData", adminData)
                    startActivity(orderHistoryPage)
                }
            }
        true })
        popupMenu.show()
    }

    fun editProfileAdminPageByText(v: View){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        val editProfileAdminByText = Intent(this, EditProfileAdminActivity::class.java)
        editProfileAdminByText.putExtra("adminData", adminData)
        startActivity(editProfileAdminByText)
    }

    fun editProfileAdminPageByButton(v: View){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        val editProfileAdminByText = Intent(this, EditProfileAdminActivity::class.java)
        editProfileAdminByText.putExtra("adminData", adminData)
        startActivity(editProfileAdminByText)
    }

    fun logoutFromAdminPage(v: View){
        val loginPage = Intent(this, LoginActivity::class.java)
        loginPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(loginPage)
        finish()
    }
}