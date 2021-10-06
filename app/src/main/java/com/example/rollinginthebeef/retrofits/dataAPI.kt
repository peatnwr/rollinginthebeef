package com.example.rollinginthebeef.retrofits

import com.example.rollinginthebeef.modules.Order
import com.example.rollinginthebeef.modules.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface dataAPI {
    @GET("allproduct")
    fun retrieveProduct(): Call<List<Product>>

    @GET("order/{user_id}")
    fun retrieveOrder(
        @Path("user_id") user_id: Int
    ): Call<List<Order>>
}