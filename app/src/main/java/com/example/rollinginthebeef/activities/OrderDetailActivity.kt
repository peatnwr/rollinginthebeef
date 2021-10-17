package com.example.rollinginthebeef.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.retrofits.OrderAPI
import com.example.rollinginthebeef.retrofits.ProductAPI
import com.example.rollinginthebeef.adapter.ReceiptAdapter
import com.example.rollinginthebeef.dataclass.*
import com.example.rollinginthebeef.databinding.ActivityOrderDetailBinding
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var bindingOrderDetail: ActivityOrderDetailBinding
    var receiptList = arrayListOf<ReceiptProduct>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingOrderDetail = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(bindingOrderDetail.root)

        var accountData = intent.extras
        var account: Account? = accountData?.getParcelable("accountData")
        var order_id = intent.getStringExtra("order_id")

        bindingOrderDetail.receiptId.text = "Order ID : " + order_id
        bindingOrderDetail.name.text = "Customer Name : " + account?.username

        bindingOrderDetail.btnBack.setOnClickListener {
            var intent = Intent(this@OrderDetailActivity, MainActivity::class.java)
            intent.putExtra("accountData", Account(account?.username, account?.password))
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        callReceiptDetail()
        callReceiptProduct()
        getReceiptImage()
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
                bindingOrderDetail.receiptRV.adapter = ReceiptAdapter(receiptList, applicationContext)
                bindingOrderDetail.receiptRV.layoutManager = LinearLayoutManager(applicationContext)
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
                bindingOrderDetail.TotalPrice2.text = response.body()?.order_total.toString()
                bindingOrderDetail.receiptDate.text = "Order Date: " + timeFormatter(response.body()?.order_date.toString())
            }

            override fun onFailure(call: Call<ReceiptDetail>, t: Throwable) {
                return t.printStackTrace()
            }

        })
    }

    fun getReceiptImage(){
        var order_id = intent.getStringExtra("order_id").toString().toInt()
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val serv: OrderAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderAPI::class.java)
        serv.getReceiptImage(
            order_id
        ).enqueue(object : Callback<ReceiptImage>{
            override fun onResponse(call: Call<ReceiptImage>, response: Response<ReceiptImage>) {
                if (response.isSuccessful){
                    val storageRef = FirebaseStorage.getInstance().reference.child("images/${response.body()?.receipt_image}")
                    val localFile = File.createTempFile("tempImage", "jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        if(progressDialog.isShowing){progressDialog.dismiss()}
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        bindingOrderDetail.receiptImage.setImageBitmap(bitmap)
                    }.addOnFailureListener {
                        if (progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }
                        Log.d("Exception Failure: ", "Failure Listener Image Receive")
                    }
                }else{
                    Toast.makeText(applicationContext,"Failed to retrieve the image", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ReceiptImage>, t: Throwable) {
                Toast.makeText(applicationContext,"Failed to retrieve the image", Toast.LENGTH_LONG).show()
            }

        })
    }
}