package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityDeliveryStatusAdminBinding
import com.example.rollinginthebeef.dataclass.DeliveryStatus
import com.example.rollinginthebeef.adapter.DeliveryStatusAdapter
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.dataAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeliveryStatusAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeliveryStatusAdminBinding
    val deliveryStatusList = arrayListOf<DeliveryStatus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeliveryStatusAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        binding.recyclerViewDeliveryStatus.adapter = DeliveryStatusAdapter(this.deliveryStatusList, applicationContext)
        binding.recyclerViewDeliveryStatus.layoutManager = LinearLayoutManager(applicationContext)

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.selectedItemId = R.id.miDeliveryStatus
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miHomeAdmin -> {
                    val mainAdminPage = Intent(this@DeliveryStatusAdminActivity, MainAdminActivity::class.java)
                    mainAdminPage.putExtra("adminData", adminData)
                    startActivity(mainAdminPage)
                }
                R.id.miAddProduct -> {
                    val addProductAdminPage = Intent(this@DeliveryStatusAdminActivity, AddProductAdminActivity::class.java)
                    addProductAdminPage.putExtra("adminData", adminData)
                    startActivity(addProductAdminPage)
                }
                R.id.miUserAdmin -> {
                    val profileAdminPage = Intent(this@DeliveryStatusAdminActivity, ProfileAdminActivity::class.java)
                    profileAdminPage.putExtra("adminData", adminData)
                    startActivity(profileAdminPage)
                }
            }
            true
        }

        binding.orderAdmin.setOnClickListener {
            val orderAdmin = Intent(this@DeliveryStatusAdminActivity, OrderAdminActivity::class.java)
            orderAdmin.putExtra("adminData", adminData)
            startActivity(orderAdmin)
        }
    }

    override fun onResume() {
        callDeliveryStatusData()
        super.onResume()
    }

    fun callDeliveryStatusData(){
        deliveryStatusList.clear()
        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)
        api.retrieveStatusDelivery().enqueue(object : Callback<List<DeliveryStatus>> {
            override fun onResponse(call: Call<List<DeliveryStatus>>, response: Response<List<DeliveryStatus>>) {
                response.body()?.forEach {
                    deliveryStatusList.add(DeliveryStatus(it.user_id, it.user_name, it.user_address, it.order_id, it.order_date, it.order_total, it.order_status, it.order_tracking))
                }
                binding.recyclerViewDeliveryStatus.adapter = DeliveryStatusAdapter(deliveryStatusList, applicationContext)
            }

            override fun onFailure(call: Call<List<DeliveryStatus>>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }
}