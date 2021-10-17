package com.example.rollinginthebeef.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.activities.OrderHistoryDetailAdminActivity
import com.example.rollinginthebeef.databinding.OrderHistoryAdminItemLayoutBinding
import com.example.rollinginthebeef.dataclass.OrderHistoryAdmin
import com.example.rollinginthebeef.modules.dateFormatter
import com.example.rollinginthebeef.dataclass.infoUserParcel

class OrderHistoryAdminAdapter(val orderHistoryList: ArrayList<OrderHistoryAdmin>, val adminData: infoUserParcel?, val context: Context) :RecyclerView.Adapter<OrderHistoryAdminAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: OrderHistoryAdminItemLayoutBinding) : RecyclerView.ViewHolder(view){
        init {
            binding.btnToOrderHistoryDetail.setOnClickListener {
                val item = orderHistoryList[adapterPosition]
                val contextView: Context = view.context
                val orderHistoryDetailPage = Intent(contextView, OrderHistoryDetailAdminActivity::class.java)
                orderHistoryDetailPage.putExtra("adminData", adminData)
                orderHistoryDetailPage.putExtra("userId", item.user_id.toString())
                orderHistoryDetailPage.putExtra("orderId", item.order_id.toString())
                contextView.startActivity(orderHistoryDetailPage)
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