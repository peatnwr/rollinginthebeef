package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReceiptDetail(

    @Expose
    @SerializedName("order_date") val order_date: String,

    @Expose
    @SerializedName("order_total") val order_total: Float

) {
}