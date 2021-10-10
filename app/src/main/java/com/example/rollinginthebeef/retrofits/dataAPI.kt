package com.example.rollinginthebeef.retrofits

import com.example.rollinginthebeef.modules.Order
import com.example.rollinginthebeef.modules.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface dataAPI {
    @GET("allproduct")
    fun retrieveProduct(): Call<List<Product>>

    @GET("order")
    fun retrieveOrder(): Call<List<Order>>
}