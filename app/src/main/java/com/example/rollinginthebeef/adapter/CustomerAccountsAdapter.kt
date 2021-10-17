package com.example.rollinginthebeef.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.activities.ProfileAdminActivity
import com.example.rollinginthebeef.databinding.CustomerAccountItemLayoutBinding
import com.example.rollinginthebeef.dataclass.CustomerAccounts
import com.example.rollinginthebeef.dataclass.LoginUser
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.authenticationAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomerAccountsAdapter(val customerAccountsList : ArrayList<CustomerAccounts>, val adminData : infoUserParcel?, val context: Context) :RecyclerView.Adapter<CustomerAccountsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: CustomerAccountItemLayoutBinding) :RecyclerView.ViewHolder(view){
        init {
            binding.btnSureDelete.setOnClickListener {
                val item = customerAccountsList[adapterPosition]
                val contextView : Context = view.context
                val alertDialog = AlertDialog.Builder(contextView)
                with(alertDialog){
                    setTitle("Are you sure")
                    setPositiveButton("No") { dialog, which -> }
                    setNegativeButton("Yes") { dialog, which ->
                        val api: authenticationAPI = Retrofit.Builder()
                            .baseUrl("http://10.0.2.2:3000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(authenticationAPI::class.java)
                        api.deleteCustomer(
                            item.user_id.toString().toInt()
                        ).enqueue(object : Callback<LoginUser> {
                            override fun onResponse(call: Call<LoginUser>, response: Response<LoginUser>) {
                                if(response.isSuccessful){
                                    Toast.makeText(contextView, "Deleted Successful", Toast.LENGTH_SHORT).show()
                                    val profileAdmin = Intent(contextView, ProfileAdminActivity::class.java)
                                    profileAdmin.putExtra("adminData", adminData)
                                    contextView.startActivity(profileAdmin)
                                }
                            }

                            override fun onFailure(call: Call<LoginUser>, t: Throwable) {
                                return t.printStackTrace()
                            }
                        })
                    }
                }
                alertDialog.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomerAccountItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        binding.nameTx.text = "Name : " + customerAccountsList!![position].user_name
        binding.telTx.text = "Tel : " + customerAccountsList!![position].user_tel
        binding.emailTx.text = "Email : " + customerAccountsList!![position].user_email
        binding.addressTx.text = "Address : " + customerAccountsList!![position].user_address
    }

    override fun getItemCount(): Int {
        return customerAccountsList!!.size
    }
}