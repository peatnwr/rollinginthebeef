package com.example.rollinginthebeef.retrofits

import com.example.rollinginthebeef.modules.Product
import retrofit2.Call
import retrofit2.http.GET

interface dataAPI {
    @GET("allproduct")
    fun retrieveProduct(): Call<List<Product>>
}