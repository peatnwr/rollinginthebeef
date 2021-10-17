package com.example.rollinginthebeef.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.databinding.ActivityCustomerAccountBinding
import com.example.rollinginthebeef.dataclass.CustomerAccounts
import com.example.rollinginthebeef.adapter.CustomerAccountsAdapter
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.dataAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomerAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerAccountBinding
    var customerAccountsList = arrayListOf<CustomerAccounts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")

        binding.btnBackToProfileAdminPage.setOnClickListener {
            val profilePage = Intent(this@CustomerAccountActivity, ProfileAdminActivity::class.java)
            profilePage.putExtra("adminData", adminData)
            startActivity(profilePage)
        }

        binding.recyclerViewCustomer.adapter = CustomerAccountsAdapter(this.customerAccountsList, adminData, applicationContext)
        binding.recyclerViewCustomer.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        callCustomerData()
    }

    fun callCustomerData(){
        val data = intent.extras
        val adminData: infoUserParcel? = data?.getParcelable("adminData")
        customerAccountsList.clear()
        val api: dataAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dataAPI::class.java)
        api.retrieveCustomer().enqueue(object: Callback<List<CustomerAccounts>> {
            override fun onResponse(call: Call<List<CustomerAccounts>>, response: Response<List<CustomerAccounts>>) {
                response.body()?.forEach {
                    customerAccountsList.add(CustomerAccounts(it.user_name, it.user_tel, it.user_email, it.user_address, it.user_id))
                }
                binding.recyclerViewCustomer.adapter = CustomerAccountsAdapter(customerAccountsList, adminData, applicationContext)
            }

            override fun onFailure(call: Call<List<CustomerAccounts>>, t: Throwable) {
                return t.printStackTrace()
            }
        })
    }
}