package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.databinding.ActivityOrderHistoryRiderBinding
import com.example.rollinginthebeef.dataclass.OrderHistoryRider
import com.example.rollinginthebeef.adapter.OrderHistoryRiderAdapter
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.dataAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderHistoryRiderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderHistoryRiderBinding
    var orderHistoryList = arrayListOf<OrderHistoryRider>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderHistoryRiderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val riderData: infoUserParcel? = data?.getParcelable("riderData")

        binding.btnBackToProfileRider.setOnClickListener {
            val profileRider = Intent(this@OrderHistoryRiderActivity, ProfileRiderActivity::class.java)
            profileRider.putExtra("riderData", riderData)
            startActivity(profileRider)
        }

        binding.recyclerViewOrderHistory.adapter = OrderHistoryRiderAdapter(this.orderHistoryList, riderData, applicationContext)
        binding.recyclerViewOrderHistory.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        callHistoryData()
    }

    fun callHistoryData(){
        val data = intent.extras
        val riderData: infoUserParcel? = data?.getParcelable("riderData")
        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)
        api.retrieveOrderHistoryRider().enqueue(object : Callback<List<OrderHistoryRider>> {
            override fun onResponse(call: Call<List<OrderHistoryRider>>, response: Response<List<OrderHistoryRider>>) {
                response.body()?.forEach {
                    orderHistoryList.add(OrderHistoryRider(it.order_id, it.order_date, it.order_total, it.user_id, it.user_name))
                }
                binding.recyclerViewOrderHistory.adapter = OrderHistoryRiderAdapter(orderHistoryList, riderData, applicationContext)
            }

            override fun onFailure(call: Call<List<OrderHistoryRider>>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }
}