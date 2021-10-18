package com.example.rollinginthebeef.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.activities.OrderDetailActivity
import com.example.rollinginthebeef.databinding.OrderHistoryUserItemLayoutBinding
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.OrderHistory
import com.example.rollinginthebeef.databinding.OrderItemLayoutBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderHistoryAdapter(var orderList: ArrayList<OrderHistory>?, val account: Account?, val context: Context)
    :RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>(){
    inner class ViewHolder(view: View, val binding: OrderHistoryUserItemLayoutBinding)
        :RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = OrderHistoryUserItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    fun timeFormatter(dateOrder: String): String{
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'.000Z'", Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
            val date = simpleDateFormat.parse(dateOrder)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return dateFormat.format(date)
        } catch (e: Exception) {
            return ""
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        binding.orderId.text = "Order ID : " + orderList!![position].order_id.toString()
        binding.userName.text = "Customer : " + account?.username
        binding.orderDate.text = "Order Date : " + timeFormatter(orderList!![position].order_date)
        if (orderList!![position].order_status == 5) {
            binding.orderTotal.text = "Canceled"
            binding.viewOrder.isVisible = false
        }else{
            binding.orderTotal.text = "Total : " + orderList!![position].order_total.toString() + " ฿"
            binding.viewOrder.setOnClickListener {
                var intent = Intent(context, OrderDetailActivity::class.java)
                intent.putExtra("accountData", Account(account?.username,account?.password))
                intent.putExtra("order_id", orderList!![position].order_id.toString())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return orderList!!.size
    }


}