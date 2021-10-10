package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.databinding.ActivityConfirmOrderPaymentBinding
import com.example.rollinginthebeef.modules.OrderDetail
import com.example.rollinginthebeef.modules.ProductListConfirmPageAdapter
import com.example.rollinginthebeef.modules.dateFormatter
import com.example.rollinginthebeef.modules.timeFormatter
import com.example.rollinginthebeef.retrofits.dataAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfirmOrderPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmOrderPaymentBinding
    val orderDetailList = arrayListOf<OrderDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfirmOrderPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackToOrderAdmin.setOnClickListener {
            val orderAdminPage = Intent(this@ConfirmOrderPaymentActivity, OrderAdminActivity::class.java)
            startActivity(orderAdminPage)
        }

        binding.orderList.adapter = ProductListConfirmPageAdapter(this.orderDetailList, applicationContext)
        binding.orderList.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        callReceiptDetail()
        super.onResume()
    }

    fun callReceiptDetail(){
        val userID = intent.getStringExtra("userID")
        val orderID = intent.getStringExtra("orderID")
        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)
        api.retrieveDetail(
            orderID.toString(),
            userID.toString()
        ).enqueue(object : Callback<List<OrderDetail>>{
            override fun onResponse(call: Call<List<OrderDetail>>, response: Response<List<OrderDetail>>) {
                response.body()?.forEach {
                    orderDetailList.add(OrderDetail(it.order_id, it.order_date, it.user_name, it.user_address, it.orderdetail_qty, it.product_name, it.orderdetail_price, it.order_total))
                    binding.receiptId.text = "Order ID : " + it.order_id.toString()
                    binding.receiptDate.text = "Date " + dateFormatter(it.order_date)
                    binding.name.text = "Customer Name : " + it.user_name
                    binding.send.text = "Send To : " + it.user_address
                    binding.totalPrice.text = it.order_total.toString()
                    binding.receiptTime.text = "Receipt Time : " + timeFormatter(it.order_date) + " น."
                }
                binding.orderList.adapter = ProductListConfirmPageAdapter(orderDetailList, applicationContext)
            }

            override fun onFailure(call: Call<List<OrderDetail>>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }
}