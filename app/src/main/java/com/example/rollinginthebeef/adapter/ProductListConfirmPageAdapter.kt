package com.example.rollinginthebeef.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.databinding.ConfirmOrderItemLayoutBinding
import com.example.rollinginthebeef.dataclass.OrderDetail

class ProductListConfirmPageAdapter(val orderDetailList: ArrayList<OrderDetail>, val context: Context) : RecyclerView.Adapter<ProductListConfirmPageAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: ConfirmOrderItemLayoutBinding) : RecyclerView.ViewHolder(view)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ConfirmOrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        binding.productAmount.text = orderDetailList!![position].orderdetail_qty.toString()
        binding.productName.text = orderDetailList!![position].product_name
        binding.productPrice.text = orderDetailList!![position].orderdetail_price.toString()
    }

    override fun getItemCount(): Int {
        return orderDetailList!!.size
    }
}