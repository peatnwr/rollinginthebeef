package com.example.rollinginthebeef.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.databinding.NotificationItemLayoutBinding
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.OrderHistory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotificationAdapter(var orderList: ArrayList<OrderHistory>?, val context: Context)
    : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: NotificationItemLayoutBinding)
        :RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.ViewHolder {
        val binding = NotificationItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        val binding = holder.binding
        if (orderList!![position].order_tracking == 1){
            binding.status.text = "Verified"
            binding.detail.text = "Your order ${orderList!![position].order_id} is on packing process ${orderList!![position].receipt_time}"
            Glide.with(this.context).load(R.drawable.bock_color).into(binding.iconStatus)
        }
        if (orderList!![position].order_tracking == 2){
            val now = Date()
            val transferFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            var tranferTime = transferFormatter.format(now)
            binding.status.text = "Shipping"
            binding.detail.text = "Your order ${orderList!![position].order_id} is on the way"
            Glide.with(this.context).load(R.drawable.ic_shipping).into(binding.iconStatus)
        }
        if (orderList!![position].order_status == 4 && orderList!![position].order_tracking == 3){
            binding.status.text = "Successful"
            binding.detail.text = "Your order ${orderList!![position].order_id} was successful at ${timeFormatter(orderList!![position].order_received_time)}"
            Glide.with(this.context).load(R.drawable.product_color).into(binding.iconStatus)
        }
    }

    fun timeFormatter(dateOrder: String): String{
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'.000Z'", Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
            val date = simpleDateFormat.parse(dateOrder)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
            return dateFormat.format(date)
        } catch (e: Exception) {
            return ""
        }
    }

    override fun getItemCount(): Int {
        return orderList!!.size
    }
}