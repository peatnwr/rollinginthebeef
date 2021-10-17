package com.example.rollinginthebeef.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.activities.DeliveryStatusUpdateActivity
import com.example.rollinginthebeef.databinding.DeliveryStatusRiderItemLayoutBinding
import com.example.rollinginthebeef.dataclass.DeliveryStatus
import com.example.rollinginthebeef.modules.dateFormatter
import com.example.rollinginthebeef.dataclass.infoUserParcel

class DeliveryStatusRiderAdapter(val deliveryStatusList: ArrayList<DeliveryStatus>, val riderData: infoUserParcel?, val context: Context) :RecyclerView.Adapter<DeliveryStatusRiderAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: DeliveryStatusRiderItemLayoutBinding) :RecyclerView.ViewHolder(view){
        init {
            binding.btnToUpdateDeliveryStatus.setOnClickListener {
                val item = deliveryStatusList[adapterPosition]
                val contextView : Context = view.context
                val updateStatus = Intent(contextView, DeliveryStatusUpdateActivity::class.java)
                updateStatus.putExtra("riderData", riderData)
                updateStatus.putExtra("userId", item.user_id.toString())
                updateStatus.putExtra("orderId", item.order_id.toString())
                contextView.startActivity(updateStatus)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DeliveryStatusRiderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        var dateMessage = ""
        var trackingMessage = ""

        if(deliveryStatusList!![position].order_tracking.toString().equals("1")){
            trackingMessage = "Waiting for Transportation"
        }else if(deliveryStatusList!![position].order_tracking.toString().equals("2")){
            trackingMessage = "Shiping"
        }

        deliveryStatusList?.forEach{
            if(it.order_date != null){ dateMessage = dateFormatter(it.order_date) }
        }

        binding.txOrderID.text = "Order ID : " + deliveryStatusList!![position].order_id
        binding.txCustomer.text = "Customer : " + deliveryStatusList!![position].user_name
        binding.txOrderStatus.text = "Status : $trackingMessage"
        binding.txAddress.text = "Address : " + deliveryStatusList!![position].user_address
        binding.txDate.text = "Order Date : $dateMessage"
        binding.txPrice.text = "Total : " + deliveryStatusList!![position].order_total.toString() + " ฿"
    }

    override fun getItemCount(): Int {
        return deliveryStatusList!!.size
    }
}