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
import com.example.rollinginthebeef.databinding.SetPermissionsItemLayoutBinding
import com.example.rollinginthebeef.dataclass.LoginUser
import com.example.rollinginthebeef.dataclass.UserPermission
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.example.rollinginthebeef.retrofits.authenticationAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SetPermissionsAdapter(val userPermission : ArrayList<UserPermission>, val adminData: infoUserParcel?, val context: Context) :RecyclerView.Adapter<SetPermissionsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: SetPermissionsItemLayoutBinding) :RecyclerView.ViewHolder(view){
        init {
            binding.btnSureDelete.setOnClickListener {
                val item = userPermission[adapterPosition]
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
                            api.deletePermissionUser(
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
        val binding = SetPermissionsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        binding.nameTx.text = "Name : " + userPermission!![position].user_name
        binding.telTx.text = "Tel : " + userPermission!![position].user_tel
        binding.emailTx.text = "Email : " + userPermission!![position].user_email
        binding.addressTx.text = "Address : " + userPermission!![position].user_address
        if(userPermission!![position].user_type.equals(1)){
            binding.positionTx.text = "Position : Admin"
        }
        else if(userPermission!![position].user_type.equals(2)){
            binding.positionTx.text = "Position : Delivery Man"
        }
    }

    override fun getItemCount(): Int {
        return userPermission!!.size
    }
}