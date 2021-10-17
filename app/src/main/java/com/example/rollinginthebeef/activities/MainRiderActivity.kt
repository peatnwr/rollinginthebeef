package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityMainRiderBinding
import com.example.rollinginthebeef.dataclass.DeliveryStatus
import com.example.rollinginthebeef.adapter.DeliveryStatusRiderAdapter
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.dataAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainRiderActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainRiderBinding
    val deliveryStatusList = arrayListOf<DeliveryStatus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainRiderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val riderData: infoUserParcel? = data?.getParcelable("riderData")

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.selectedItemId = R.id.miDeliveryStatusRider
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.miUserRider -> {
                    val profileRider = Intent(this@MainRiderActivity, ProfileRiderActivity::class.java)
                    profileRider.putExtra("riderData", riderData)
                    startActivity(profileRider)
                }
            }
            true
        }

        binding.recyclerViewDeliveryStatus.adapter = DeliveryStatusRiderAdapter(this.deliveryStatusList, riderData, applicationContext)
        binding.recyclerViewDeliveryStatus.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        callDeliveryStatusData()
    }

    fun callDeliveryStatusData(){
        val data = intent.extras
        val riderData: infoUserParcel? = data?.getParcelable("riderData")
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
                binding.recyclerViewDeliveryStatus.adapter = DeliveryStatusRiderAdapter(deliveryStatusList, riderData, applicationContext)
            }

            override fun onFailure(call: Call<List<DeliveryStatus>>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }
}