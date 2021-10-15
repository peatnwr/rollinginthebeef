package com.example.rollinginthebeef.modules

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.databinding.OrderHistoryItemLayoutBinding

class ProductOrderHistoryRiderAdapter(val orderHistoryDetailList: ArrayList<OrderHistoryDetail>, val context: Context) :RecyclerView.Adapter<ProductOrderHistoryRiderAdapter.ViewHolder>() {
    inner class ViewHolder(view : View, val binding: OrderHistoryItemLayoutBinding) : RecyclerView.ViewHolder(view){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderHistoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        binding.productAmount.text = orderHistoryDetailList!![position].orderdetail_qty.toString()
        binding.productName.text = orderHistoryDetailList!![position].product_name
        binding.productPrice.text = orderHistoryDetailList!![position].orderdetail_price.toString()
    }

    override fun getItemCount(): Int {
        return orderHistoryDetailList!!.size
    }
}