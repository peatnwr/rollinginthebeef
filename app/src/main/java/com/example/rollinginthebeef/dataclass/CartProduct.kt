package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CartProduct(
    @Expose
    @SerializedName("orderdetail_qty") val order_qty: Int,

    @Expose
    @SerializedName("category_name") val category_name: String,

    @Expose
    @SerializedName("product_name") val product_name: String,

    @Expose
    @SerializedName("product_price") val product_price: Float,

    @Expose
    @SerializedName("product_img") val product_image: String?,

    @Expose
    @SerializedName("total") val total: Float

) {

}