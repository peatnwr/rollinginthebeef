package com.example.rollinginthebeef.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.activities.DetailActivity
import com.example.rollinginthebeef.databinding.ProductItemLayoutBinding
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.CartItem
import com.example.rollinginthebeef.dataclass.ProductMain
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ProductUserAdapter(var productList: ArrayList<ProductMain>?, val account: Account?, val context: Context) :RecyclerView.Adapter<ProductUserAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: ProductItemLayoutBinding) :RecyclerView.ViewHolder(view){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        binding.title.text = productList!![position].product_name
        binding.type.text = productList!![position].category_name
        binding.price.text = productList!![position].product_price.toString() + " ฿"
        val storageRef = FirebaseStorage.getInstance().reference.child("product/${productList!![position].product_img}")
        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.productImg.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Log.d("Exception Failure: ", "Failure Listener Image Receive")
        }
        binding.itemCard.setOnClickListener{
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("cartItem", CartItem(productList!![position].product_name, productList!![position].product_detail,
                productList!![position].product_price, productList!![position].category_name,
                productList!![position].product_id, productList!![position].product_img, productList!![position].product_qty)
            )
            intent.putExtra("account", Account(account?.username, account?.password))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productList!!.size
    }
}