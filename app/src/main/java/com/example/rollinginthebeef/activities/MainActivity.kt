package com.example.rollinginthebeef.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rollinginthebeef.*
import com.example.rollinginthebeef.retrofits.OrderAPI
import com.example.rollinginthebeef.retrofits.authenticationAPI
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.OrderID
import com.example.rollinginthebeef.dataclass.ProductMain
import com.example.rollinginthebeef.fragment.DeliveryStatusFragment
import com.example.rollinginthebeef.fragment.MainFragment
import com.example.rollinginthebeef.fragment.ProfileFragment
import com.example.rollinginthebeef.databinding.ActivityMainBinding
import com.example.rollinginthebeef.fragment.NotificationFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var productList = arrayListOf<ProductMain>()
    var searchList = arrayListOf<ProductMain>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var accountData = intent.extras
        var account: Account? = accountData?.getParcelable("accountData")
        Log.d("Username : ", account?.username.toString())


        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miHome -> {
                    val bundle =  Bundle()
                    bundle.putParcelable("accountData", account)
                    val fragmentMain = MainFragment()
                    fragmentMain.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        fragmentMain
                    ).commit()
                }
                R.id.miList -> {
                    val bundle =  Bundle()
                    bundle.putParcelable("accountData", account)
                    val fragmentStatus = DeliveryStatusFragment()
                    fragmentStatus.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        fragmentStatus
                    ).commit()
                }
                R.id.miBell -> {
                    val bundle =  Bundle()
                    bundle.putParcelable("accountData", account)
                    val fragmentNotification = NotificationFragment()
                    fragmentNotification.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        fragmentNotification
                    ).commit()
                }
                R.id.miUser -> {
                    val bundle =  Bundle()
                    bundle.putParcelable("accountData", account)
                    val fragmentStatus = ProfileFragment()
                    fragmentStatus.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        fragmentStatus
                    ).commit()
                }
            }
            true
        }

        binding.fad.setOnClickListener {
            getOrderId(account)
        }

    }

    override fun onResume() {
        super.onResume()
        createOrder()
        binding.bottomNavigationView.selectedItemId = R.id.miHome
    }


    fun getOrderId(account: Account?){
        val serv: OrderAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderAPI::class.java)
        serv.getOrderId(
            account?.username.toString()
        ).enqueue(object: Callback<OrderID>{
            override fun onResponse(call: Call<OrderID>, response: Response<OrderID>) {
                var intent = Intent(this@MainActivity, CartActivity::class.java)
                intent.putExtra("accountData", Account(account?.username, account?.password))
                intent.putExtra("order_id", response.body()?.order_id.toString())
                startActivity(intent)
            }

            override fun onFailure(call: Call<OrderID>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun createOrder(){
        var accountData = intent.extras
        var account: Account? = accountData?.getParcelable("accountData")
        val ser: authenticationAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(authenticationAPI::class.java)

        ser.createOrder(account?.username.toString())
            .enqueue(object : Callback<OrderID> {
                override fun onResponse(call: Call<OrderID>, response: Response<OrderID>) {
                }
                override fun onFailure(call: Call<OrderID>, t: Throwable) {
                }
            })

    }


}