package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityMainAdminBinding
import com.example.rollinginthebeef.dataclass.Product
import com.example.rollinginthebeef.adapter.ProductAdapter
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.dataAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainAdminBinding
    var productList = arrayListOf<Product>()
    var searchList = arrayListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.selectedItemId = R.id.miHomeAdmin
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miDeliveryStatus -> {
                    val deliveryStatusAdminPage = Intent(this@MainAdminActivity, DeliveryStatusAdminActivity::class.java)
                    deliveryStatusAdminPage.putExtra("adminData", adminData)
                    startActivity(deliveryStatusAdminPage)
                }
                R.id.miAddProduct -> {
                    val addProductAdminPage = Intent(this@MainAdminActivity, AddProductAdminActivity::class.java)
                    addProductAdminPage.putExtra("adminData", adminData)
                    startActivity(addProductAdminPage)
                }
                R.id.miUserAdmin -> {
                    val profileAdminPage = Intent(this@MainAdminActivity, ProfileAdminActivity::class.java)
                    profileAdminPage.putExtra("adminData", adminData)
                    startActivity(profileAdminPage)
                }
            }
            true
        }

        binding.recyclerViewAdmin.adapter = ProductAdapter(this.searchList, applicationContext)
        binding.recyclerViewAdmin.layoutManager = LinearLayoutManager(applicationContext)

        binding.orderAdmin.setOnClickListener {
            val orderAdmin = Intent(this@MainAdminActivity, OrderAdminActivity::class.java)
            orderAdmin.putExtra("adminData", adminData)
            startActivity(orderAdmin)
        }

        binding.SearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchList.clear()
                val searchTx = query!!.toLowerCase(Locale.getDefault())
                if(searchTx.isNotEmpty()){
                    productList.forEach {
                        if(it.product_name.toLowerCase(Locale.getDefault()).contains(searchTx) || it.category_name.toLowerCase(Locale.getDefault()).contains(searchTx)) {
                            searchList.add(it)
                        }
                    }
                    binding.recyclerViewAdmin.adapter!!.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(productList)
                    binding.recyclerViewAdmin.adapter!!.notifyDataSetChanged()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchTx = newText!!.toLowerCase(Locale.getDefault())
                if (searchTx.isNotEmpty()) {
                    productList.forEach {
                        if (it.product_name.toLowerCase(Locale.getDefault()).contains(searchTx) || it.category_name.toLowerCase(Locale.getDefault()).contains(searchTx)) {
                            searchList.add(it)
                        }
                    }
                    binding.recyclerViewAdmin.adapter!!.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(productList)
                    binding.recyclerViewAdmin.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })
    }

    override fun onResume() {
        callProductData()
        super.onResume()
    }

    fun callProductData(){
        productList.clear()
        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)
        api.retrieveProduct()
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    response.body()?.forEach {
                        productList.add(Product(it.product_id, it.product_name, it.product_price, it.product_detail, it.product_img, it.product_category, it.category_name))
                    }
                    searchList.addAll(productList)
                    binding.recyclerViewAdmin.adapter = ProductAdapter(searchList, applicationContext)
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    return t.printStackTrace()
                }
            })
    }
}