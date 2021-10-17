package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.adapter.ProductDeliveryInfoAdapter
import com.example.rollinginthebeef.databinding.ActivityDeliveryStatusUpdateBinding
import com.example.rollinginthebeef.dataclass.DeliveryStatusUpdate
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.modules.*
import com.example.rollinginthebeef.retrofits.dataAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class DeliveryStatusUpdateActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityDeliveryStatusUpdateBinding
    val deliveryInfoList = arrayListOf<DeliveryStatusUpdate>()
    var deliveryStatus : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeliveryStatusUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val riderData: infoUserParcel? = data?.getParcelable("riderData")

        binding.btnBackToDeliveryStatus.setOnClickListener {
            val mainRider = Intent(this@DeliveryStatusUpdateActivity, MainRiderActivity::class.java)
            mainRider.putExtra("riderData", riderData)
            startActivity(mainRider)
        }

        binding.orderList.adapter = ProductDeliveryInfoAdapter(deliveryInfoList, applicationContext)
        binding.orderList.layoutManager = LinearLayoutManager(applicationContext)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.delivery_status,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val itemSpinner: String = parent?.getItemAtPosition(position).toString()
        deliveryStatus = itemSpinner
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        callDeliveryInformation()
    }

    fun callDeliveryInformation(){
        val userId = intent.getStringExtra("userId")
        val orderId = intent.getStringExtra("orderId")
        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)
        api.retrieveDeliveryInfo(
            orderId.toString(),
            userId.toString()
        ).enqueue(object : Callback<List<DeliveryStatusUpdate>> {
            override fun onResponse(call: Call<List<DeliveryStatusUpdate>>, response: Response<List<DeliveryStatusUpdate>>) {
                response.body()?.forEach {
                    deliveryInfoList.add(DeliveryStatusUpdate(it.order_id, it.order_date, it.user_name, it.user_address, it.orderdetail_qty, it.product_name, it.orderdetail_price, it.order_total, it.order_status, it.order_tracking))
                    binding.receiptId.text = "Order ID : " + it.order_id.toString()
                    binding.receiptDate.text = "Date : " + dateFormatter(it.order_date)
                    binding.name.text = "Customer : " + it.user_name
                    binding.send.text = "Send To : " + it.user_address
                    binding.totalPrice.text = it.order_total.toString()
                }
                binding.orderList.adapter = ProductDeliveryInfoAdapter(deliveryInfoList, applicationContext)

            }

            override fun onFailure(call: Call<List<DeliveryStatusUpdate>>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }

    fun updateStatus(v: View){
        val data = intent.extras
        val riderData: infoUserParcel? = data?.getParcelable("riderData")
        val orderId = intent.getStringExtra("orderId")
        var statusDelivery : Int? = 0

        deliveryStatus?.forEach {
            when(deliveryStatus){
                "Shiping" -> { statusDelivery = 2 }
                "Successful" -> { statusDelivery = 3 }
            }
        }

        val sdf = SimpleDateFormat("yyyy-M-dd hh:mm:ss")
        val currentDate = sdf.format(Date())
        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)
        api.updateDeliveryStatus(
            orderId.toString(),
            statusDelivery,
            currentDate
        ).enqueue(object : Callback<DeliveryStatusUpdate> {
            override fun onResponse(call: Call<DeliveryStatusUpdate>, response: Response<DeliveryStatusUpdate>) {
                if(response.isSuccessful){
                    val mainRider = Intent(this@DeliveryStatusUpdateActivity, MainRiderActivity::class.java)
                    mainRider.putExtra("riderData", riderData)
                    startActivity(mainRider)
                }
            }

            override fun onFailure(call: Call<DeliveryStatusUpdate>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }
}