package com.example.rollinginthebeef.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.dataclass.ReceiptProduct
import com.example.rollinginthebeef.databinding.ReceiptItemLayoutBinding

class ReceiptAdapter(var receiptList: ArrayList<ReceiptProduct>?, val context: Context)
    : RecyclerView.Adapter<ReceiptAdapter.ViewHolder>(){
    inner class ViewHolder(view: View, val binding: ReceiptItemLayoutBinding)
        :RecyclerView.ViewHolder(view){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReceiptItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        binding.productAmount.text = receiptList!![position].order_qty.toString()
        binding.productName.text = receiptList!![position].product_name
        binding.productPrice.text = receiptList!![position].total.toString()
    }

    override fun getItemCount(): Int {
        return receiptList!!.size
    }
}