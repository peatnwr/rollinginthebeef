package com.example.rollinginthebeef.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.databinding.ConfirmOrderItemLayoutBinding
import com.example.rollinginthebeef.dataclass.DeliveryStatusUpdate

class ProductDeliveryInfoAdapter(val deliveryInfoList: ArrayList<DeliveryStatusUpdate>, val context: Context) :RecyclerView.Adapter<ProductDeliveryInfoAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: ConfirmOrderItemLayoutBinding) :RecyclerView.ViewHolder(view){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ConfirmOrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        binding.productAmount.text = deliveryInfoList!![position].orderdetail_qty.toString()
        binding.productName.text = deliveryInfoList!![position].product_name
        binding.productPrice.text = deliveryInfoList!![position].orderdetail_price.toString()
    }

    override fun getItemCount(): Int {
        return deliveryInfoList!!.size
    }
}