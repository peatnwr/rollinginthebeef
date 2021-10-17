package com.example.rollinginthebeef.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.*
import com.example.rollinginthebeef.retrofits.OrderAPI
import com.example.rollinginthebeef.retrofits.ProductAPI
import com.example.rollinginthebeef.adapter.CartAdapter
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.CartProduct
import com.example.rollinginthebeef.dataclass.OrderID
import com.example.rollinginthebeef.dataclass.OrderHistory
import com.example.rollinginthebeef.databinding.ActivityCartBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    var cartList = arrayListOf<CartProduct>()
    var total: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var accountData = intent.extras
        var account: Account? = accountData?.getParcelable("accountData")
        var order_id = intent.getStringExtra("order_id")

        binding.cartRV.adapter = CartAdapter(this.cartList, account , this@CartActivity, order_id.toString().toInt(), applicationContext)
        binding.cartRV.layoutManager = LinearLayoutManager(applicationContext)
        binding.back.setOnClickListener{
            var intent = Intent(this@CartActivity, MainActivity::class.java)
            intent.putExtra("accountData", Account(account?.username, account?.password))
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        var accountData = intent.extras
        var account: Account? = accountData?.getParcelable("accountData")
        callActiveOrder(account)
        callCartData()
    }

    fun confirmOrder(account: Account?, date: String, time: String, total: Double){
        val serv: OrderAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderAPI::class.java)
        serv.confirmOrder(
            account?.username.toString(),
            date,
            time,
            total
        ).enqueue(object : Callback<OrderID>{
            override fun onResponse(call: Call<OrderID>, response: Response<OrderID>) {
                Toast.makeText(applicationContext, "Confirmed Order", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<OrderID>, t: Throwable) {
                Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun callActiveOrder(account:Account?){
        var order_id = intent.getStringExtra("order_id")
        val serv: OrderAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderAPI::class.java)
        serv.getActiveOrder(
            account?.username.toString()
        ).enqueue(object: Callback<OrderHistory>{
            override fun onResponse(call: Call<OrderHistory>, response: Response<OrderHistory>) {
                if (response.isSuccessful){

                    binding.confirmBtn.setBackgroundColor(Color.DKGRAY)
                    binding.confirmBtn.isEnabled = false
                    binding.confirmBtn.setOnClickListener {
                        Toast.makeText(applicationContext, "Already Have Order", Toast.LENGTH_LONG).show()
                    }
                }else{
                    binding.confirmBtn.setOnClickListener {
                        val sdf = SimpleDateFormat("yyyy/M/dd hh:mm:ss")
                        val ts = SimpleDateFormat("hh:mm:ss")
                        val currentDate = sdf.format(Date())
                        val timestamp = ts.format(Date())
                        confirmOrder(account, currentDate, timestamp, total)
                        var intent = Intent(this@CartActivity, SendReceiptActivity::class.java)
                        intent.putExtra("accountData", Account(account?.username, account?.password))
                        intent.putExtra("order_id", order_id)
                        startActivity(intent)
                    }
                }
            }
            override fun onFailure(call: Call<OrderHistory>, t: Throwable) {
                Toast.makeText(applicationContext, "Already Have Order", Toast.LENGTH_LONG).show()
                binding.confirmBtn.isEnabled = false
            }
        })
    }

    fun callCartData(){
        cartList.clear()
        var accountData = intent.extras
        var account: Account? = accountData?.getParcelable("accountData")
        var order_id = intent.getStringExtra("order_id")
        val serv: ProductAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductAPI::class.java)
        serv.retrieveCart(
            order_id.toString().toInt()
        ).enqueue(object : Callback<List<CartProduct>> {
            override fun onResponse(
                call: Call<List<CartProduct>>,
                response: Response<List<CartProduct>>
            ) {
                response.body()?.forEach{
                    cartList.add(CartProduct(it.order_qty, it.category_name, it.product_name, it.product_price.toFloat(),it.product_image,it.total.toFloat()))
                }
                cartList.forEach{
                    total += it.total
                }
                if (total == 0.0){
                    binding.confirmBtn.isEnabled = false
                }else if(total != 0.0){
                    binding.confirmBtn.isEnabled = true
                }
                binding.totalPrice.text = total.toString()
                binding.cartRV.adapter = CartAdapter(cartList ,account ,this@CartActivity, order_id.toString().toInt(),applicationContext)
                binding.cartRV.layoutManager = LinearLayoutManager(applicationContext)
            }
            override fun onFailure(call: Call<List<CartProduct>>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }
}