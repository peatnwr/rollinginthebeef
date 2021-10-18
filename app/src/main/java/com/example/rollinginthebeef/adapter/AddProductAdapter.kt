package com.example.rollinginthebeef.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rollinginthebeef.activities.ProductEditAdmin
import com.example.rollinginthebeef.databinding.AddProductItemLayoutBinding
import com.example.rollinginthebeef.dataclass.AddProduct
import com.example.rollinginthebeef.dataclass.infoUserParcel
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class AddProductAdapter(val addProductList: ArrayList<AddProduct>, val context: Context, val adminData: infoUserParcel?)
    : RecyclerView.Adapter<AddProductAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: AddProductItemLayoutBinding) : RecyclerView.ViewHolder(view){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AddProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val storageRef = FirebaseStorage.getInstance().reference.child("product/${addProductList!![position].product_img}")
        val localFile = File.createTempFile("tempImage", "jpg")

        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            Glide.with(context).load(bitmap).into(binding.productImg)
        }.addOnFailureListener{
            Log.d("Exception Failure: ", "Failure Listener Image Receive")
        }
        binding.productName.text = addProductList!![position].product_name
        binding.categoryName.text = addProductList!![position].category_name
        binding.stockItem.text = "Stock : " + addProductList!![position].product_qty
        binding.productPrice.text = addProductList!![position].product_price.toString() + " bath"
        binding.productCard.setOnClickListener{
            var intent = Intent(context, ProductEditAdmin::class.java)
            intent.putExtra("adminData", adminData)
            intent.putExtra("productName", addProductList!![position].product_name)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return addProductList!!.size
    }
}