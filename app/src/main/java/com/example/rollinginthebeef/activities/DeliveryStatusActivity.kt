package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rollinginthebeef.retrofits.ProductAPI
import com.example.rollinginthebeef.adapter.ReceiptAdapter
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.ReceiptDetail
import com.example.rollinginthebeef.dataclass.ReceiptProduct
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityDeliveryStatusBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class DeliveryStatusActivity : AppCompatActivity() {

    private lateinit var bindingDeliveryStatus: ActivityDeliveryStatusBinding
    var receiptList = arrayListOf<ReceiptProduct>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDeliveryStatus = ActivityDeliveryStatusBinding.inflate(layoutInflater)
        setContentView(bindingDeliveryStatus.root)

        var accountData = intent.extras
        var account: Account? = accountData?.getParcelable("accountData")
        var order_id = intent.getStringExtra("order_id")
        var status = intent.getStringExtra("status")

        bindingDeliveryStatus.receiptId.text = "Order ID : " + order_id
        bindingDeliveryStatus.name.text = "Customer Name : " + account?.username
        bindingDeliveryStatus.textStatus.text = "Status : " + status
        if (status.equals("Waiting For Confirmation")){
            Glide.with(applicationContext).load(R.drawable.ic_list).into(bindingDeliveryStatus.image)
        }
        if (status.equals("Waiting For Transportation")){
            Glide.with(applicationContext).load(R.drawable.bock_color).into(bindingDeliveryStatus.image)
        }
        if (status.equals("Shipping")){
            Glide.with(applicationContext).load(R.drawable.ic_shipping).into(bindingDeliveryStatus.image)
        }

        bindingDeliveryStatus.btnBack.setOnClickListener {
            var intent = Intent(this@DeliveryStatusActivity, MainActivity::class.java)
            intent.putExtra("accountData", Account(account?.username, account?.password))
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        callReceiptDetail()
        callReceiptProduct()
    }

    fun timeFormatter(dateOrder: String): String{
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'.000Z'", Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
            val date = simpleDateFormat.parse(dateOrder)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return dateFormat.format(date)
        } catch (e: Exception) {
            return ""
        }
    }

    fun callReceiptProduct(){
        receiptList.clear()
        var order_id = intent.getStringExtra("order_id")
        val serv: ProductAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductAPI::class.java)
        serv.receiptProduct(
            order_id.toString().toInt()
        ).enqueue(object : Callback<List<ReceiptProduct>> {
            override fun onResponse(call: Call<List<ReceiptProduct>>, response: Response<List<ReceiptProduct>>) {
                response.body()?.forEach {
                    receiptList.add(ReceiptProduct(it.order_qty, it.product_name, it.total))
                }
                bindingDeliveryStatus.receiptRV.adapter = ReceiptAdapter(receiptList, applicationContext)
                bindingDeliveryStatus.receiptRV.layoutManager = LinearLayoutManager(applicationContext)
            }
            override fun onFailure(call: Call<List<ReceiptProduct>>, t: Throwable) {
                return t.printStackTrace()
            }

        })
    }
    fun callReceiptDetail(){
        var order_id = intent.getStringExtra("order_id")
        val serv: ProductAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductAPI::class.java)
        serv.receiptDetail(
            order_id.toString().toInt()
        ).enqueue(object: Callback<ReceiptDetail> {
            override fun onResponse(call: Call<ReceiptDetail>, response: Response<ReceiptDetail>) {
                bindingDeliveryStatus.TotalPrice2.text = response.body()?.order_total.toString()
                bindingDeliveryStatus.receiptDate.text = "Order Date: " + timeFormatter(response.body()?.order_date.toString())
            }

            override fun onFailure(call: Call<ReceiptDetail>, t: Throwable) {
                return t.printStackTrace()
            }

        })
    }
}