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
import com.example.rollinginthebeef.modules.infoUserParcel

class ProfileAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileAdminBinding.inflate(layoutInflater)
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
        binding.bottomNavigationView.selectedItemId = R.id.miUserAdmin
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miHomeAdmin -> {
                    val mainAdminPage = Intent(this@ProfileAdminActivity, MainAdminActivity::class.java)
                    mainAdminPage.putExtra("adminData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
                    startActivity(mainAdminPage)
                }
                R.id.miDeliveryStatus -> {
                    val deliveryStatusAdminPage = Intent(this@ProfileAdminActivity, DeliveryStatusAdminActivity::class.java)
                    deliveryStatusAdminPage.putExtra("adminData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
                    startActivity(deliveryStatusAdminPage)
                }
                R.id.miAddProduct -> {
                }
            }
            true
        }

        val edtNameText: EditText = findViewById(R.id.edtNameAdminPage)
        val edtEmailText: EditText = findViewById(R.id.edtEmailAdminPage)
        val edtTelText: EditText = findViewById(R.id.edtTelAdminPage)
        val edtAddressText: EditText = findViewById(R.id.edtAddressAdminPage)
        val edtUsernameText: EditText = findViewById(R.id.edtUsernameAdminPage)

        binding.textUsername.text = nameUser
        edtNameText.setText(nameUser)
        edtEmailText.setText(userEmail)
        edtTelText.setText(userTel)
        edtAddressText.setText(userAddress)
        edtUsernameText.setText(userName)

    }

    fun showOptionAdminPage(v: View){
        val popupMenu = PopupMenu(this, binding.btnOptionAdminPage)
        popupMenu.inflate(R.menu.menu_admin)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
            item: MenuItem? ->
            when(item!!.itemId){
                R.id.menuSetPermission -> {
                    val setPermissionsPage = Intent(this, SetPermissionsActivity::class.java)
                    startActivity(setPermissionsPage)
                }
                R.id.menuCustomerAccount -> {
                    val customerAccountPage = Intent(this, CustomerAccountActivity::class.java)
                    startActivity(customerAccountPage)
                }
                R.id.menuOrderHistory -> {
                    val orderHistoryPage = Intent(this, OrderHistoryActivity::class.java)
                    startActivity(orderHistoryPage)
                }
            }
        true })
        popupMenu.show()
    }

    fun editProfileAdminPageByText(v: View){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        val userID = adminData?.userID.toString().toInt()
        val userName = adminData?.userName.toString()
        val userTel = adminData?.userTel.toString()
        val userAddress = adminData?.userAddress.toString()
        val userEmail = adminData?.userEmail.toString()
        val nameUser = adminData?.nameUser.toString()

        val editProfileAdminByText = Intent(this, EditProfileAdminActivity::class.java)
        editProfileAdminByText.putExtra("adminData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
        startActivity(editProfileAdminByText)
    }

    fun editProfileAdminPageByButton(v: View){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        val userID = adminData?.userID.toString().toInt()
        val userName = adminData?.userName.toString()
        val userTel = adminData?.userTel.toString()
        val userAddress = adminData?.userAddress.toString()
        val userEmail = adminData?.userEmail.toString()
        val nameUser = adminData?.nameUser.toString()

        val editProfileAdminByText = Intent(this, EditProfileAdminActivity::class.java)
        editProfileAdminByText.putExtra("adminData", infoUserParcel(userID, userName, userTel, userAddress, userEmail, nameUser))
        startActivity(editProfileAdminByText)
    }

    fun logoutFromAdminPage(v: View){
        val loginPage = Intent(this, LoginActivity::class.java)
        loginPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(loginPage)
        finish()
    }
}