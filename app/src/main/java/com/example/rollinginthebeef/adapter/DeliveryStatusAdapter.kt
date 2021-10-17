package com.example.rollinginthebeef.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.databinding.DeliveryStatusItemLayoutBinding
import com.example.rollinginthebeef.dataclass.DeliveryStatus
import com.example.rollinginthebeef.modules.dateFormatter

class DeliveryStatusAdapter(val deliveryStatusList: ArrayList<DeliveryStatus>, val context: Context) : RecyclerView.Adapter<DeliveryStatusAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: DeliveryStatusItemLayoutBinding) : RecyclerView.ViewHolder(view){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DeliveryStatusItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        var dateMessage = ""
        var statusMessage = ""

        if(deliveryStatusList!![position].order_tracking.toString().equals("1")){
            statusMessage = "Waiting for Transportation"
        }else if(deliveryStatusList!![position].order_tracking.toString().equals("2")){
            statusMessage = "Shiping"
        }

        deliveryStatusList?.forEach{
            if(it.order_date != null){ dateMessage = dateFormatter(it.order_date) }
        }

        binding.txOrderID.text = "Order ID : " + deliveryStatusList!![position].order_id
        binding.txCustomer.text = "Customer : " + deliveryStatusList!![position].user_name
        binding.txOrderStatus.text = "Status : $statusMessage"
        binding.txAddress.text = "Address : " + deliveryStatusList!![position].user_address
        binding.txDate.text = "Order Date : $dateMessage"
        binding.txPrice.text = "Total : " + deliveryStatusList!![position].order_total.toString() + " ฿"
    }

    override fun getItemCount(): Int {
        return deliveryStatusList!!.size
    }
}