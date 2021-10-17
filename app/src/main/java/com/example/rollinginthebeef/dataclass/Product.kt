package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Product (
    @Expose
    @SerializedName("product_id") val product_id: Int,

    @Expose
    @SerializedName("product_name") val product_name: String,

    @Expose
    @SerializedName("product_price") val product_price: Float,

    @Expose
    @SerializedName("product_detail") val product_detail: String,

    @Expose
    @SerializedName("product_img") val product_img: String,

    @Expose
    @SerializedName("category_id") val product_category: Int,

    @Expose
    @SerializedName("category_name") val category_name: String
){}
