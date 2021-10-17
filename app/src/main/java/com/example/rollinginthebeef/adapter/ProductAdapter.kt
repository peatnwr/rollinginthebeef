package com.example.rollinginthebeef.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rollinginthebeef.databinding.ProductItemLayoutBinding
import com.example.rollinginthebeef.dataclass.Product
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ProductAdapter (val productList: ArrayList<Product>?, val context: Context)
    : RecyclerView.Adapter<ProductAdapter.ViewHolder>(){
    inner class ViewHolder(view: View, val binding: ProductItemLayoutBinding)
        : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val storageRef = FirebaseStorage.getInstance().reference.child("product/${productList!![position].product_img}")
        val localFile = File.createTempFile("tempImage", "jpg")
        binding.title.text = productList!![position].product_name
        binding.type.text = productList!![position].category_name
        binding.price.text = productList!![position].product_price.toString() + " ฿"
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            Glide.with(context).load(bitmap).into(binding.productImg)
        }.addOnFailureListener{
            Log.d("Exception Failure: ", "Failure Listener Image Receive")
        }
    }

    override fun getItemCount(): Int {
        return productList!!.size
    }
}