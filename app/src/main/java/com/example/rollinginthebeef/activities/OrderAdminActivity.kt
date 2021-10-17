package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityOrderAdminBinding
import com.example.rollinginthebeef.dataclass.Order
import com.example.rollinginthebeef.adapter.OrderAdapter
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.dataAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderAdminBinding
    val orderList = arrayListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.selectedItemId = R.id.placeholder
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miHomeAdmin -> {
                    val mainAdminPage = Intent(this@OrderAdminActivity, MainAdminActivity::class.java)
                    mainAdminPage.putExtra("adminData", adminData)
                    startActivity(mainAdminPage)
                }
                R.id.miDeliveryStatus -> {
                    val deliveryStatusAdminPage = Intent(this@OrderAdminActivity, DeliveryStatusAdminActivity::class.java)
                    deliveryStatusAdminPage.putExtra("adminData", adminData)
                    startActivity(deliveryStatusAdminPage)
                }
                R.id.miAddProduct -> {
                    val addProductAdminPage = Intent(this@OrderAdminActivity, AddProductAdminActivity::class.java)
                    addProductAdminPage.putExtra("adminData", adminData)
                    startActivity(addProductAdminPage)
                }
                R.id.miUserAdmin -> {
                    val profileAdminPage = Intent(this@OrderAdminActivity, ProfileAdminActivity::class.java)
                    profileAdminPage.putExtra("adminData", adminData)
                    startActivity(profileAdminPage)
                }
            }
            true
        }

        binding.recyclerViewAdminOrder.adapter = OrderAdapter(this.orderList, adminData, applicationContext)
        binding.recyclerViewAdminOrder.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        callOrderData()
        super.onResume()
    }

    fun callOrderData(){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")
        orderList.clear()
        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)
        api.retrieveOrder().enqueue(object : Callback<List<Order>>{
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                response.body()?.forEach {
                    orderList.add(Order(it.user_id, it.user_username, it.user_name, it.order_id, it.order_date, it.order_total, it.order_status))
                }
                binding.recyclerViewAdminOrder.adapter = OrderAdapter(orderList, adminData, applicationContext)
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }
}