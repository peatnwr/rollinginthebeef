package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReceiptProduct(
    @Expose
    @SerializedName("orderdetail_qty") val order_qty: Int,

    @Expose
    @SerializedName("product_name") val product_name: String,

    @Expose
    @SerializedName("total") val total: Float
) {
}