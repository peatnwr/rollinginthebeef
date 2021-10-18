package com.example.rollinginthebeef.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.*
import com.example.rollinginthebeef.retrofits.OrderAPI
import com.example.rollinginthebeef.retrofits.ProductAPI
import com.example.rollinginthebeef.adapter.ReceiptAdapter
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.OrderID
import com.example.rollinginthebeef.dataclass.ReceiptDetail
import com.example.rollinginthebeef.dataclass.ReceiptProduct
import com.example.rollinginthebeef.fragment.TimePickerFragment
import com.example.rollinginthebeef.databinding.ActivitySendReceiptBinding
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class SendReceiptActivity : AppCompatActivity() {

    private lateinit var sendReceiptBinding: ActivitySendReceiptBinding
    private lateinit var ImageUri: Uri
    var receiptList = arrayListOf<ReceiptProduct>()
    var tranferTime : String? = ""
    var checkReceipt : String? = ""
    var checkTranfer : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendReceiptBinding = ActivitySendReceiptBinding.inflate(layoutInflater)
        setContentView(sendReceiptBinding.root)
        var data = intent.extras
        var account: Account? = data?.getParcelable("accountData")
        var order_id = intent.getStringExtra("order_id")
        sendReceiptBinding.receiptName.text = "Customer Name : " + account?.username
        sendReceiptBinding.receiptId.text = "Order ID : " + order_id
        sendReceiptBinding.uploadImage.isEnabled = false

        sendReceiptBinding.back.setOnClickListener {
            var intent = Intent(this@SendReceiptActivity, MainActivity::class.java)
            intent.putExtra("order_id", order_id.toString())
            intent.putExtra("accountData", Account(account?.username, account?.password))
            startActivity(intent)
        }

        sendReceiptBinding.cancelOrder.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Cancel Order")
            dialogBuilder.setMessage("Are you sure to cancel this order ?")
            dialogBuilder.setPositiveButton("yes") { dialog, which ->
                cancelOrder(order_id.toString().toInt(), account)
            }
            dialogBuilder.setNegativeButton("no") { dialog, which ->
                dialog.cancel()
            }
            dialogBuilder.show()

        }

        sendReceiptBinding.btnAddImg.setOnClickListener {
            selectImage()
        }
        sendReceiptBinding.uploadImage.setOnClickListener {
            uploadImage()
        }
        sendReceiptBinding.tranferTime.setOnClickListener {
            val newTimeFragment = TimePickerFragment()
            newTimeFragment.show(supportFragmentManager, "Time Picker")

        }
    }

    override fun onResume() {
        super.onResume()
        checkTime()
        callReceiptProduct()
        callReceiptDetail()
        if (checkReceipt.equals("1") && checkTranfer.equals("1")){
            sendReceiptBinding.uploadImage.isEnabled = true
        }
    }

    fun checkTime(){
        checkTranfer = "1"
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

    fun selectImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
        checkReceipt = "1"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            sendReceiptBinding.receiptImg.setImageURI(ImageUri)
        }
    }

    fun uploadImage(){
        var data = intent.extras
        var order_id = intent.getStringExtra("order_id")
        var account: Account? = data?.getParcelable("accountData")
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading File ...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val transferFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var tranferTime = transferFormatter.format(now)
        val filename = formatter.format(now) + order_id
        val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")

        storageReference.putFile(ImageUri).
        addOnSuccessListener {
            sendReceiptBinding.receiptImg.setImageURI(null)
            Toast.makeText(applicationContext,"Successfully Uploaded", Toast.LENGTH_LONG).show()
            if (progressDialog.isShowing) progressDialog.dismiss()

        }.addOnFailureListener{
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(applicationContext, "Upload Failed", Toast.LENGTH_LONG).show()
        }

        val serv: OrderAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderAPI::class.java)
        serv.uploadImage(
            order_id.toString().toInt(),
            filename,
            tranferTime.toString()
        ).enqueue(object: Callback<OrderID>{
            override fun onResponse(call: Call<OrderID>, response: Response<OrderID>) {
            }
            override fun onFailure(call: Call<OrderID>, t: Throwable) {
                return t.printStackTrace()
            }
        })
        var intent = Intent(this@SendReceiptActivity, MainActivity::class.java)
        intent.putExtra("accountData", Account(account?.username, account?.password))
        startActivity(intent)
    }

    fun cancelOrder(order_id: Int, account: Account?){
        val serv: OrderAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderAPI::class.java)
        serv.cancelOrder(
            order_id
        ).enqueue(object : Callback<OrderID>{
            override fun onResponse(call: Call<OrderID>, response: Response<OrderID>) {
                Toast.makeText(applicationContext, "Canceled Order", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@SendReceiptActivity, MainActivity::class.java)
                intent.putExtra("order_id", order_id.toString())
                intent.putExtra("accountData", Account(account?.username, account?.password))
                startActivity(intent)
            }
            override fun onFailure(call: Call<OrderID>, t: Throwable) {
                Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
            }
        })
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
        ).enqueue(object : Callback<List<ReceiptProduct>>{
            override fun onResponse(call: Call<List<ReceiptProduct>>, response: Response<List<ReceiptProduct>>) {
                response.body()?.forEach {
                    receiptList.add(ReceiptProduct(it.order_qty, it.product_name, it.total))
                }
                sendReceiptBinding.receiptRV.adapter = ReceiptAdapter(receiptList, applicationContext)
                sendReceiptBinding.receiptRV.layoutManager = LinearLayoutManager(applicationContext)
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
        ).enqueue(object: Callback<ReceiptDetail>{
            override fun onResponse(call: Call<ReceiptDetail>, response: Response<ReceiptDetail>) {
                sendReceiptBinding.receiptDate.text = "Order Date: " + timeFormatter(response.body()?.order_date.toString())
                sendReceiptBinding.TotalPrice2.text = response.body()?.order_total.toString()
            }

            override fun onFailure(call: Call<ReceiptDetail>, t: Throwable) {
                return t.printStackTrace()
            }

        })
    }
}