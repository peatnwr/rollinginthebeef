package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.retrofits.OrderAPI
import com.example.rollinginthebeef.adapter.OrderHistoryAdapter
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.OrderHistory
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityOrderHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var bindingHistory: ActivityOrderHistoryBinding
    var orderList = arrayListOf<OrderHistory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingHistory = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(bindingHistory.root)

        var accountData = intent.extras
        var account: Account? = accountData?.getParcelable("accountData")

        bindingHistory.bottomNavigationView.background = null
        bindingHistory.bottomNavigationView.menu.getItem(2).isEnabled = false
        bindingHistory.bottomNavigationView.selectedItemId = R.id.miList
        bindingHistory.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miHome -> {
                    var intent = Intent(this@OrderHistoryActivity, MainActivity::class.java)
                    intent.putExtra("userData", account)
                    startActivity(intent)
                }
            }
            true
        }

    }

    override fun onResume() {
        super.onResume()
        callOrderHistory()
    }

    fun callOrderHistory(){
        var accountData = intent.extras
        var account: Account? = accountData?.getParcelable("accountData")
        val serv: OrderAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderAPI::class.java)
        serv.getOrderHistory(
            account?.username.toString()
        ).enqueue(object : Callback<List<OrderHistory>> {
            override fun onResponse(call: Call<List<OrderHistory>>, response: Response<List<OrderHistory>>) {
                response.body()?.forEach {
                    orderList.add(OrderHistory(it.order_id, it.order_date, it.order_time, it.order_total, it.order_status, it.order_tracking, it.order_received_time, it.receipt_img, it.receipt_time, it.user_id))
                }
                bindingHistory.recyclerView.adapter = OrderHistoryAdapter(orderList, account, applicationContext)
                bindingHistory.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
            }

            override fun onFailure(call: Call<List<OrderHistory>>, t: Throwable) {
                Toast.makeText(applicationContext, "Falied", Toast.LENGTH_SHORT).show()

            }

        })
    }
}