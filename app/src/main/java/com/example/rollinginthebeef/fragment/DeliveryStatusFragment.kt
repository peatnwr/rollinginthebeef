package com.example.rollinginthebeef.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.retrofits.OrderAPI
import com.example.rollinginthebeef.activities.DeliveryStatusActivity
import com.example.rollinginthebeef.activities.SendReceiptActivity
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.OrderHistory
import com.example.rollinginthebeef.adapter.OrderHistoryAdapter
import com.example.rollinginthebeef.databinding.FragmentDeliveryStatusBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class DeliveryStatusFragment : Fragment() {

    private lateinit var bindingDeliveryStatus: FragmentDeliveryStatusBinding
    var orderList = arrayListOf<OrderHistory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingDeliveryStatus = FragmentDeliveryStatusBinding.inflate(layoutInflater)
        return bindingDeliveryStatus.root
    }

    override fun onResume() {
        super.onResume()
        var bundle = arguments
        var account = bundle!!.getParcelable<Account?>("accountData")
        callOrderHistory(account)
        callActiveOrder(account)
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

    fun callOrderHistory(account: Account?){
        val serv: OrderAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderAPI::class.java)
        serv.getOrderHistory(
            account?.username.toString()
        ).enqueue(object : Callback<List<OrderHistory>> {
            override fun onResponse(call: Call<List<OrderHistory>>, response: Response<List<OrderHistory>>) {
                response.body()?.forEach {
                    orderList.add(OrderHistory(it.order_id, it.order_date, it.order_time, it.order_total, it.order_status, it.order_tracking, it.order_received_time, it.receipt_img, it.receipt_time, it.user_id))
                }
                bindingDeliveryStatus.recyclerView.adapter = OrderHistoryAdapter(orderList, account, requireContext())
                bindingDeliveryStatus.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }

            override fun onFailure(call: Call<List<OrderHistory>>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun callActiveOrder(account:Account?){
        val serv: OrderAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderAPI::class.java)
        serv.getActiveOrder(
            account?.username.toString()
        ).enqueue(object: Callback<OrderHistory>{
            override fun onResponse(call: Call<OrderHistory>, response: Response<OrderHistory>) {
                if (response.isSuccessful){
                    bindingDeliveryStatus.orderId.text = "Order ID : " + response.body()?.order_id.toString()
                    bindingDeliveryStatus.userName.text = "Customer : " + account?.username.toString()
                    bindingDeliveryStatus.orderDate.text = "Order Date : " + timeFormatter(response.body()?.order_date.toString())
                    bindingDeliveryStatus.orderTotal.text = response.body()?.order_total.toString()
                }
                if (response.body()?.order_status == 1){
                    bindingDeliveryStatus.orderStatus.text = "Status : Pending"
                    bindingDeliveryStatus.goToOrder.setOnClickListener {
                        var intent = Intent(requireContext(), SendReceiptActivity::class.java)
                        intent.putExtra("accountData", Account(account?.username, account?.password))
                        intent.putExtra("order_id", response.body()?.order_id.toString())
                        startActivity(intent)
                    }
                }
                if (response.body()?.order_status == 2){
                    bindingDeliveryStatus.orderStatus.text = "Status : Waiting For Confirmation"
                    bindingDeliveryStatus.goToOrder.setOnClickListener {
                        var intent = Intent(context, DeliveryStatusActivity::class.java)
                        intent.putExtra("accountData", Account(account?.username,account?.password))
                        intent.putExtra("order_id",response.body()?.order_id.toString())
                        intent.putExtra("status", "Waiting For Confirmation")
                        startActivity(intent)
                    }
                }
                if (response.body()?.order_tracking == 1){
                    bindingDeliveryStatus.orderStatus.text = "Status : Waiting For Transportation"
                    bindingDeliveryStatus.goToOrder.setOnClickListener {
                        var intent = Intent(context, DeliveryStatusActivity::class.java)
                        intent.putExtra("accountData", Account(account?.username,account?.password))
                        intent.putExtra("order_id",response.body()?.order_id.toString())
                        intent.putExtra("status", "Waiting For Transportation")
                        startActivity(intent)
                    }
                }
                if (response.body()?.order_tracking == 2){
                    bindingDeliveryStatus.orderStatus.text = "Status : Shipping"
                    bindingDeliveryStatus.goToOrder.setOnClickListener {
                        var intent = Intent(context, DeliveryStatusActivity::class.java)
                        intent.putExtra("accountData", Account(account?.username,account?.password))
                        intent.putExtra("order_id",response.body()?.order_id.toString())
                        intent.putExtra("status", "Shipping")
                        startActivity(intent)
                    }
                }
            }
            override fun onFailure(call: Call<OrderHistory>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }


}