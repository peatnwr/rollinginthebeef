package com.example.rollinginthebeef.modules

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rollinginthebeef.databinding.ProductItemLayoutBinding

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
        binding.title.text = productList!![position].product_name
        binding.type.text = productList!![position].category_name
        binding.price.text = productList!![position].product_price.toString() + " ฿"
        Glide.with(context).load(productList!![position].product_img).into(binding.productImg)
    }

    override fun getItemCount(): Int {
        return productList!!.size
    }
}