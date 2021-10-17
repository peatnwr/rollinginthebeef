package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.databinding.ActivityOrderHistoryAdminBinding
import com.example.rollinginthebeef.dataclass.OrderHistoryAdmin
import com.example.rollinginthebeef.adapter.OrderHistoryAdminAdapter
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.dataAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderHistoryAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderHistoryAdminBinding
    var orderHistoryList = arrayListOf<OrderHistoryAdmin>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderHistoryAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        binding.btnBackToProfileAdminPage.setOnClickListener {
            val profileAdmin = Intent(this@OrderHistoryAdminActivity, ProfileAdminActivity::class.java)
            profileAdmin.putExtra("adminData", adminData)
            startActivity(profileAdmin)
        }

        binding.recyclerViewOrderHistory.adapter = OrderHistoryAdminAdapter(this.orderHistoryList, adminData, applicationContext)
        binding.recyclerViewOrderHistory.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        callOrderHistoryData()
    }

    fun callOrderHistoryData(){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")
        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)
        api.retrieveOrderHistory().enqueue(object: Callback<List<OrderHistoryAdmin>> {
            override fun onResponse(call: Call<List<OrderHistoryAdmin>>, response: Response<List<OrderHistoryAdmin>>) {
                response.body()?.forEach {
                    orderHistoryList.add(OrderHistoryAdmin(it.order_id, it.order_date, it.order_total, it.user_id, it.user_name))
                }
                binding.recyclerViewOrderHistory.adapter = OrderHistoryAdminAdapter(orderHistoryList, adminData, applicationContext)
            }

            override fun onFailure(call: Call<List<OrderHistoryAdmin>>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }
}