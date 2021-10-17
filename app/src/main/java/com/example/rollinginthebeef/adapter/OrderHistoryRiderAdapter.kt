package com.example.rollinginthebeef.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.activities.OrderHistoryDetailRiderActivity
import com.example.rollinginthebeef.databinding.OrderHistoryAdminItemLayoutBinding
import com.example.rollinginthebeef.dataclass.OrderHistoryRider
import com.example.rollinginthebeef.modules.dateFormatter
import com.example.rollinginthebeef.dataclass.infoUserParcel

class OrderHistoryRiderAdapter(val orderHistoryList: ArrayList<OrderHistoryRider>, val riderData: infoUserParcel?, val context: Context) :RecyclerView.Adapter<OrderHistoryRiderAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: OrderHistoryAdminItemLayoutBinding) :RecyclerView.ViewHolder(view){
        init {
            binding.btnToOrderHistoryDetail.setOnClickListener {
                val item = orderHistoryList[adapterPosition]
                val contextView : Context = view.context
                val orderHistoryRider = Intent(contextView, OrderHistoryDetailRiderActivity::class.java)
                orderHistoryRider.putExtra("riderData", riderData)
                orderHistoryRider.putExtra("userId", item.user_id.toString())
                orderHistoryRider.putExtra("orderId", item.order_id.toString())
                contextView.startActivity(orderHistoryRider)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderHistoryAdminItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        var dateFormat = ""

        orderHistoryList.forEach {
            if(it.order_date != null){ dateFormat = dateFormatter(it.order_date) }
        }

        binding.orderIdTx.text = "Order ID : " + orderHistoryList!![position].order_id
        binding.customerTx.text = "Customer : " + orderHistoryList!![position].user_name
        binding.orderDateTx.text = "Order Date : $dateFormat"
        binding.orderPriceTx.text = "Total : " + orderHistoryList!![position].order_total + " ฿"
    }

    override fun getItemCount(): Int {
        return orderHistoryList!!.size
    }
}