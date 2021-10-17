package com.example.rollinginthebeef.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.retrofits.OrderDetailAPI
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.CartProduct
import com.example.rollinginthebeef.dataclass.ProductMain
import com.example.rollinginthebeef.databinding.CartItemLayoutBinding
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class CartAdapter(var cartList: ArrayList<CartProduct>?, val account: Account?, val v: Activity, val order_id: Int, val context: Context)
    : RecyclerView.Adapter<CartAdapter.ViewHolder>(){
    inner class ViewHolder(view: View, val binding: CartItemLayoutBinding)
        :RecyclerView.ViewHolder(view){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CartItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val binding = holder.binding
        binding.title.text = cartList!![position].product_name
        binding.type.text = cartList!![position].category_name
        binding.amount.text = cartList!![position].order_qty.toString()
        binding.price.text = cartList!![position].product_price.toString()
        val storageRef = FirebaseStorage.getInstance().reference.child("product/${cartList!![position].product_image}")
        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.productImg.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Log.d("Exception Failure: ", "Failure Listener Image Receive")
        }
        binding.add.setOnClickListener {
            addQuantity(cartList!![position].product_name)
            binding.amount.text = (binding.amount.text.toString().toInt() + 1).toString()
            binding.reduce.isEnabled = true
        }
        if (binding.amount.text.toString().toInt() == 1){
            binding.reduce.isEnabled = false
        }else{
            binding.reduce.isEnabled = true
        }
        binding.reduce.setOnClickListener {
            removeQuantity(cartList!![position].product_name)
            binding.amount.text = (binding.amount.text.toString().toInt() - 1).toString()
            if (binding.amount.text.toString().toInt() == 1){
                binding.reduce.isEnabled = false
            }
        }
        binding.delete.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(v)
            dialogBuilder.setTitle("Cancel Order")
            dialogBuilder.setMessage("Are you sure to delete product ?")
            dialogBuilder.setPositiveButton("yes") { dialog, which ->
                removeProduct(cartList!![position].product_name)

            }
            dialogBuilder.setNegativeButton("no") { dialog, which ->
                dialog.cancel()
            }
            dialogBuilder.show()
        }
    }

    override fun getItemCount(): Int {
        return cartList!!.size
    }

    fun removeProduct(name: String){
        val serv: OrderDetailAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderDetailAPI::class.java)
        serv.removeProduct(
            account?.username.toString(),
            name
        ).enqueue(object : Callback<ProductMain>{
            override fun onResponse(call: Call<ProductMain>, response: Response<ProductMain>) {
                Toast.makeText(context, "Successfully Remove Product", Toast.LENGTH_SHORT).show()
                var intent = Intent(v, v::class.java)
                intent.putExtra("accountData", Account(account?.username, account?.password))
                intent.putExtra("order_id", order_id.toString())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
            override fun onFailure(call: Call<ProductMain>, t: Throwable) {
                Toast.makeText(context, "Remove Failied", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun addQuantity(name: String){
        val serv: OrderDetailAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderDetailAPI::class.java)
        serv.addqty(
            account?.username.toString(),
            name
        ).enqueue(object : Callback<ProductMain> {
            override fun onResponse(call: Call<ProductMain>, response: Response<ProductMain>) {
                println("Successfully Added")
                var intent = Intent(v, v::class.java)
                intent.putExtra("accountData", Account(account?.username, account?.password))
                intent.putExtra("order_id", order_id.toString())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }

            override fun onFailure(call: Call<ProductMain>, t: Throwable) {
                println("Failed Add")
            }
        })
    }

    fun removeQuantity(name: String){
        val serv: OrderDetailAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderDetailAPI::class.java)
        serv.removeqty(
            account?.username.toString(),
            name
        ).enqueue(object : Callback<ProductMain> {
            override fun onResponse(call: Call<ProductMain>, response: Response<ProductMain>) {
                println("Removed")
                var intent = Intent(v, v::class.java)
                intent.putExtra("accountData", Account(account?.username, account?.password))
                intent.putExtra("order_id", order_id.toString())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }

            override fun onFailure(call: Call<ProductMain>, t: Throwable) {
                println("Failed")
            }

        })
    }
}