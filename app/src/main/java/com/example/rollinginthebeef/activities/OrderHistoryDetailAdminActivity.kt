package com.example.rollinginthebeef.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.adapter.ProductOrderHistoryDetailAdapter
import com.example.rollinginthebeef.databinding.ActivityOrderHistoryDetailAdminBinding
import com.example.rollinginthebeef.dataclass.OrderHistoryDetail
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.modules.*
import com.example.rollinginthebeef.retrofits.dataAPI
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class OrderHistoryDetailAdminActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOrderHistoryDetailAdminBinding
    val orderHistoryDetailList = arrayListOf<OrderHistoryDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderHistoryDetailAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        binding.btnBackToOrderHistoryAdmin.setOnClickListener {
            val orderHistoryAdmin = Intent(this@OrderHistoryDetailAdminActivity, OrderHistoryAdminActivity::class.java)
            orderHistoryAdmin.putExtra("adminData", adminData)
            startActivity(orderHistoryAdmin)
        }

        binding.orderList.adapter = ProductOrderHistoryDetailAdapter(this.orderHistoryDetailList, applicationContext)
        binding.orderList.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        callReceiptData()
    }

    fun callReceiptData(){
        val userId = intent.getStringExtra("userId")
        val orderId = intent.getStringExtra("orderId")
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)
        api.retrieveOrderHistoryDetail(
            orderId.toString(),
            userId.toString()
        ).enqueue(object : Callback<List<OrderHistoryDetail>> {
            override fun onResponse(call: Call<List<OrderHistoryDetail>>, response: Response<List<OrderHistoryDetail>>) {
                response.body()?.forEach {
                    orderHistoryDetailList.add(OrderHistoryDetail(it.order_id, it.order_date, it.user_name, it.user_address, it.orderdetail_qty, it.product_name, it.orderdetail_price, it.order_total, it.order_received_time, it.receipt_img))
                    binding.receiptId.text = "Order ID : " + it.order_id.toString()
                    binding.receiptDate.text = "Date " + dateFormatter(it.order_date)
                    binding.name.text = "Customer Name : " + it.user_name
                    binding.send.text = "Send To : " + it.user_address
                    binding.totalPrice.text = it.order_total.toString()
                    binding.receiptTime.text = "Receipt Time : " + timeFormatter(it.order_date) + " น."
                    binding.receivedTimeTx.text = "Received Time : " + dateFormatter(it.order_received_time) + " " + timeFormatter(it.order_received_time) + " น."
                    val storageRef = FirebaseStorage.getInstance().reference.child("images/${it.receipt_img}")
                    val localFile = File.createTempFile("tempImage", "jpg")

                    storageRef.getFile(localFile).addOnSuccessListener {
                        if(progressDialog.isShowing){progressDialog.dismiss()}
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        binding.receiptImg.setImageBitmap(bitmap)
                    }.addOnFailureListener{
                        if(progressDialog.isShowing){progressDialog.dismiss()}
                        Log.d("Exception Failure: ", "Failure Listener Image Receive")
                    }
                }
                binding.orderList.adapter = ProductOrderHistoryDetailAdapter(orderHistoryDetailList, applicationContext)
            }

            override fun onFailure(call: Call<List<OrderHistoryDetail>>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }
}