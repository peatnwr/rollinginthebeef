package com.example.rollinginthebeef.retrofits

import com.example.rollinginthebeef.modules.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface dataAPI {
    @GET("allproduct")
    fun retrieveProduct(): Call<List<Product>>

    @GET("order")
    fun retrieveOrder(): Call<List<Order>>

    @GET("orderdetail/{order_id}/{user_id}")
    fun retrieveDetail(
        @Path("order_id") order_id: String,
        @Path("user_id") user_id: String
    ): Call<List<OrderDetail>>

    @PATCH("confirmpayment/{order_id}")
    fun confirmPayment(
        @Path("order_id") order_id: String
    ): Call<OrderDetail>

    @GET("deliverystatus")
    fun retrieveStatusDelivery(): Call<List<DeliveryStatus>>

    @GET("addproductpage")
    fun retrieveAddProduct(): Call<List<AddProduct>>
}