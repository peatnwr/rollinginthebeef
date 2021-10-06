package com.example.rollinginthebeef.modules

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.databinding.OrderItemLayoutBinding

class OrderAdapter(val orderList: ArrayList<Order>?, val context: Context)
    : RecyclerView.Adapter<OrderAdapter.ViewHolder>(){
    inner class ViewHolder(view: View, val binding: OrderItemLayoutBinding)
        : RecyclerView.ViewHolder(view){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.ViewHolder {
        val binding = OrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: OrderAdapter.ViewHolder, position: Int) {
        val binding = holder.binding
        binding.txOrderID.text = orderList!![position].order_id.toString()
        binding.txCustomer.text = orderList!![position].user_username
        binding.txOrderStatus.text = orderList!![position].order_status.toString()
        binding.txOrderDate.text = orderList!![position].order_date
        binding.txOrderPrice.text = orderList!![position].order_total.toString()
    }

    override fun getItemCount(): Int {
        return orderList!!.size
    }
}