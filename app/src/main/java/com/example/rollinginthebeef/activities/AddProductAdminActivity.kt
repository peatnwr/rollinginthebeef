package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.ActivityAddProductAdminBinding
import com.example.rollinginthebeef.dataclass.AddProduct
import com.example.rollinginthebeef.adapter.AddProductAdapter
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.dataAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class AddProductAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductAdminBinding
    val addProductList = arrayListOf<AddProduct>()
    val searchList = arrayListOf<AddProduct>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddProductAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        binding.recyclerViewAddProductAdmin.adapter = AddProductAdapter(this.searchList, applicationContext, adminData)
        binding.recyclerViewAddProductAdmin.layoutManager = LinearLayoutManager(applicationContext)

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.selectedItemId = R.id.miAddProduct
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miHomeAdmin -> {
                    val mainAdminPage = Intent(this@AddProductAdminActivity, MainAdminActivity::class.java)
                    mainAdminPage.putExtra("adminData", adminData)
                    startActivity(mainAdminPage)
                }
                R.id.miDeliveryStatus -> {
                    val deliveryStatusAdminPage = Intent(this@AddProductAdminActivity, DeliveryStatusAdminActivity::class.java)
                    deliveryStatusAdminPage.putExtra("adminData", adminData)
                    startActivity(deliveryStatusAdminPage)
                }
                R.id.miUserAdmin -> {
                    val profileAdminPage = Intent(this@AddProductAdminActivity, ProfileAdminActivity::class.java)
                    profileAdminPage.putExtra("adminData", adminData)
                    startActivity(profileAdminPage)
                }
            }
            true
        }

        binding.SearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchList.clear()
                val searchTx = query!!.toLowerCase(Locale.getDefault())
                if(searchTx.isNotEmpty()){
                    addProductList.forEach {
                        if(it.product_name.toLowerCase(Locale.getDefault()).contains(searchTx) || it.category_name.toLowerCase(
                                Locale.getDefault()).contains(searchTx)) {
                            searchList.add(it)
                        }
                    }
                    binding.recyclerViewAddProductAdmin.adapter!!.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(addProductList)
                    binding.recyclerViewAddProductAdmin.adapter!!.notifyDataSetChanged()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchTx = newText!!.toLowerCase(Locale.getDefault())
                if(searchTx.isNotEmpty()){
                    addProductList.forEach {
                        if(it.product_name.toLowerCase(Locale.getDefault()).contains(searchTx) || it.category_name.toLowerCase(
                                Locale.getDefault()).contains(searchTx)) {
                            searchList.add(it)
                        }
                    }
                    binding.recyclerViewAddProductAdmin.adapter!!.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(addProductList)
                    binding.recyclerViewAddProductAdmin.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })

        binding.orderAdmin.setOnClickListener {
            val orderAdmin = Intent(this@AddProductAdminActivity, OrderAdminActivity::class.java)
            orderAdmin.putExtra("adminData", adminData)
            startActivity(orderAdmin)
        }

        binding.btnToAddProduct.setOnClickListener {
            val productAddPage = Intent(this@AddProductAdminActivity, ProductAddAdminActivity::class.java)
            productAddPage.putExtra("adminData", adminData)
            startActivity(productAddPage)
        }
    }

    override fun onResume() {
        callProductData()
        super.onResume()
    }

    fun callProductData(){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")
        addProductList.clear()
        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)
        api.retrieveAddProduct().enqueue(object: Callback<List<AddProduct>> {
            override fun onResponse(call: Call<List<AddProduct>>, response: Response<List<AddProduct>>) {
                response.body()?.forEach {
                    addProductList.add(AddProduct(it.product_name, it.product_price, it.product_img, it.product_qty, it.category_name))
                }
                searchList.addAll(addProductList)
                binding.recyclerViewAddProductAdmin.adapter = AddProductAdapter(searchList, applicationContext, adminData)
            }

            override fun onFailure(call: Call<List<AddProduct>>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }
}