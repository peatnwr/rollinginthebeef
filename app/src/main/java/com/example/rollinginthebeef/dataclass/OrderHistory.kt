package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderHistory(

    @Expose
    @SerializedName("order_id") var order_id: Int,

    @Expose
    @SerializedName("order_date") var order_date: String,

    @Expose
    @SerializedName("order_time") var order_time: String,

    @Expose
    @SerializedName("order_total") var order_total: Double,

    @Expose
    @SerializedName("order_status") var order_status: Int,

    @Expose
    @SerializedName("order_tracking") var order_tracking: Int,

    @Expose
    @SerializedName("order_received_time") var order_received_time: String,

    @Expose
    @SerializedName("receipt_img") var receipt_img: String?,

    @Expose
    @SerializedName("receipt_time") var receipt_time: String?,

    @Expose
    @SerializedName("user_id") var user_id: Int

) {
}