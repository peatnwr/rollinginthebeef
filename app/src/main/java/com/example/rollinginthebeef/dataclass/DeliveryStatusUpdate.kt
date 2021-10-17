package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeliveryStatusUpdate(
    @Expose
    @SerializedName("order_id") val order_id: Int,

    @Expose
    @SerializedName("order_date") val order_date: String,

    @Expose
    @SerializedName("user_name") val user_name: String,

    @Expose
    @SerializedName("user_address") val user_address: String,

    @Expose
    @SerializedName("orderdetail_qty") val orderdetail_qty: Int,

    @Expose
    @SerializedName("product_name") val product_name: String,

    @Expose
    @SerializedName("orderdetail_price") val orderdetail_price: Int,

    @Expose
    @SerializedName("order_total") val order_total: Int,

    @Expose
    @SerializedName("order_status") val order_status: Int,

    @Expose
    @SerializedName("order_tracking") val order_tracking: Int) {}