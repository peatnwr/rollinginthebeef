package com.example.rollinginthebeef.modules

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rollinginthebeef.databinding.AddProductItemLayoutBinding

class AddProductAdapter(val addProductList: ArrayList<AddProduct>, val context: Context) : RecyclerView.Adapter<AddProductAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: AddProductItemLayoutBinding) : RecyclerView.ViewHolder(view){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AddProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        Glide.with(context).load(addProductList!![position].product_img).into(binding.productImg)
        binding.productName.text = addProductList!![position].product_name
        binding.categoryName.text = addProductList!![position].category_name
        binding.stockItem.text = "Stock : " + addProductList!![position].product_qty
        binding.productPrice.text = addProductList!![position].product_price.toString() + " bath"
    }

    override fun getItemCount(): Int {
        return addProductList!!.size
    }
}