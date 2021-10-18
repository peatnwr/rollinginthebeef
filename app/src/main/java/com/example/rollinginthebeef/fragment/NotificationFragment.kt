package com.example.rollinginthebeef.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rollinginthebeef.R
import com.example.rollinginthebeef.adapter.NotificationAdapter
import com.example.rollinginthebeef.databinding.FragmentNotificationBinding
import com.example.rollinginthebeef.dataclass.Account
import com.example.rollinginthebeef.dataclass.Order
import com.example.rollinginthebeef.dataclass.OrderHistory
import com.example.rollinginthebeef.retrofits.OrderAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NotificationFragment : Fragment() {

    private lateinit var bindingNotification: FragmentNotificationBinding
    var orderList = arrayListOf<OrderHistory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingNotification = FragmentNotificationBinding.inflate(layoutInflater)
        return bindingNotification.root
    }

    override fun onResume() {
        var bundle = arguments
        var account = bundle!!.getParcelable<Account?>("accountData")
        getNotiOrder(account)
        super.onResume()
    }

    fun getNotiOrder(account: Account?){
        val serv: OrderAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderAPI::class.java)
        serv.getAllUserOrder(
            account?.username.toString()
        ).enqueue(object: Callback<List<OrderHistory>>{
            override fun onResponse(
                call: Call<List<OrderHistory>>,
                response: Response<List<OrderHistory>>
            ) {
                if (response.isSuccessful){
                    response.body()?.forEach {
                        orderList.add(OrderHistory(it.order_id, it.order_date, it.order_time, it.order_total, it.order_status, it.order_tracking, it.order_received_time, it.receipt_img, it.receipt_time, it.user_id))
                    }
                    bindingNotification.recyclerView.adapter = NotificationAdapter(orderList, requireContext())
                    bindingNotification.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
            }

            override fun onFailure(call: Call<List<OrderHistory>>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }
}