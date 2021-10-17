package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderHistoryAdmin(
    @Expose
    @SerializedName("order_id") val order_id: Int,

    @Expose
    @SerializedName("order_date") val order_date: String,

    @Expose
    @SerializedName("order_total") val order_total: Int,

    @Expose
    @SerializedName("user_id") val user_id: Int,

    @Expose
    @SerializedName("user_name") val user_name: String) {}