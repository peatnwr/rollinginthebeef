package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddProduct(
    @Expose
    @SerializedName("product_name") val product_name: String,

    @Expose
    @SerializedName("product_price") val product_price: Int,

    @Expose
    @SerializedName("product_img") val product_img: String,

    @Expose
    @SerializedName("product_qty") val product_qty: Int,

    @Expose
    @SerializedName("category_name") val category_name: String) {}