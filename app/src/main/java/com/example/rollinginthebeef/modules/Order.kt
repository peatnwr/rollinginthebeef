package com.example.rollinginthebeef.modules

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Order(
    @Expose
    @SerializedName("user_id") val user_id: Int,

    @Expose
    @SerializedName("user_username") val user_username: String,

    @Expose
    @SerializedName("order_id") val order_id: Int,

    @Expose
    @SerializedName("order_date") val order_date: String,

    @Expose
    @SerializedName("order_total") val order_total: Float,

    @Expose
    @SerializedName("order_status") val order_status: Int) {}
