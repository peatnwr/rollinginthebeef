package com.example.rollinginthebeef.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.rollinginthebeef.databinding.ActivityDetailBinding
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.CartItem
import com.example.rollinginthebeef.dataclass.OrderID
import com.example.rollinginthebeef.retrofits.authenticationAPI
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class DetailActivity : AppCompatActivity() {
    private lateinit var bindingDetail: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDetail = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(bindingDetail.root)
        var data = intent.extras
        var item: CartItem? = data?.getParcelable("cartItem")
        var account: Account? = data?.getParcelable("account")
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        bindingDetail.title.text = item?.name
        bindingDetail.detail.text = item?.detail
        bindingDetail.price.text = item?.price.toString()
        bindingDetail.type.text = item?.category
        val storageRef = FirebaseStorage.getInstance().reference.child("product/${item?.productImg}")
        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            if(progressDialog.isShowing){progressDialog.dismiss()}
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            bindingDetail.productImg.setImageBitmap(bitmap)
        }.addOnFailureListener {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
            Log.d("Exception Failure: ", "Failure Listener Image Receive")
        }
        bindingDetail.btnBack.setOnClickListener{
            var intent = Intent(this@DetailActivity, MainActivity::class.java)
            intent.putExtra("accountData", Account(account?.username, account?.password))
            startActivity(intent)
        }

        if (item?.product_qty == 0){
            bindingDetail.btnAdd.isEnabled = false
            bindingDetail.btnAdd.setBackgroundColor(Color.parseColor("#808080"))
            bindingDetail.btnAdd.setText("Out of stock")
        }

        bindingDetail.btnAdd.setOnClickListener {
            createOrder()
        }
    }

    fun createOrder() {
        var data = intent.extras
        var item: CartItem? = data?.getParcelable("cartItem")
        var account: Account? = data?.getParcelable("account")
        val api: authenticationAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(authenticationAPI::class.java)
        api.addOrder(
            account?.username.toString(),
            item?.productID.toString().toInt(),
            item?.price.toString().toFloat()
        ).enqueue(object : Callback<OrderID> {
            override fun onResponse(call: Call<OrderID>, response: Response<OrderID>) {
                if (response.isSuccessful()) {
                    Toast.makeText(applicationContext, "Successfully Added", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this@DetailActivity, MainActivity::class.java)
                    intent.putExtra("accountData", Account(account?.username, account?.password))
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Failed to Add" , Toast.LENGTH_SHORT).show()
                    var intent = Intent(this@DetailActivity, MainActivity::class.java)
                    intent.putExtra("accountData", Account(account?.username, account?.password))
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<OrderID>, t: Throwable) {
                Toast.makeText(applicationContext, "Successfully Added", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@DetailActivity, MainActivity::class.java)
                intent.putExtra("accountData", Account(account?.username, account?.password))
                startActivity(intent)
            }
        })
    }
}