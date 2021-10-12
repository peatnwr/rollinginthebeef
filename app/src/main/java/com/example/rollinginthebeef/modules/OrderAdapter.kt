package com.example.rollinginthebeef.modules

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.activities.ConfirmOrderPaymentActivity
import com.example.rollinginthebeef.activities.DeliveryStatusAdminActivity
import com.example.rollinginthebeef.databinding.OrderItemLayoutBinding
import kotlin.collections.ArrayList

class OrderAdapter(val orderList: ArrayList<Order>, val adminData: infoUserParcel?, val context: Context) : RecyclerView.Adapter<OrderAdapter.ViewHolder>(){
    inner class ViewHolder(view: View, val binding: OrderItemLayoutBinding) : RecyclerView.ViewHolder(view) {
        init {
            binding.btnConfirmPaymentPage.setOnClickListener {
                val item = orderList[adapterPosition]
                val contextView: Context = view.context
                val confirmPaymentPage = Intent(contextView, ConfirmOrderPaymentActivity::class.java)
                confirmPaymentPage.putExtra("adminData", adminData)
                confirmPaymentPage.putExtra("userID", item.user_id.toString())
                confirmPaymentPage.putExtra("orderID", item.order_id.toString())
                contextView.startActivity(confirmPaymentPage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        var dateFormat = ""
        var statusMessage = ""

        orderList?.forEach {
            if(it.order_date != null){ dateFormat = dateFormatter(it.order_date) }
            when(it.order_status.toString()){
                "2" -> { statusMessage = "Waiting for Confirmation" }
            }
        }

        binding.txOrderID.text = "Order ID : " + orderList!![position].order_id.toString()
        binding.txCustomer.text = "Customer : " + orderList!![position].user_name
        binding.txOrderStatus.text = "Status : $statusMessage"
        binding.txOrderDate.text = "Order Date : $dateFormat"
        binding.txOrderPrice.text = "Total : " + orderList!![position].order_total.toString() + " ฿"
    }

    override fun getItemCount(): Int {
        return orderList!!.size
    }
}