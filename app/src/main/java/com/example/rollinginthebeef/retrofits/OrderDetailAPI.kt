package com.example.rollinginthebeef.retrofits

import com.example.rollinginthebeef.dataclass.CartProduct
import com.example.rollinginthebeef.dataclass.ProductMain
import retrofit2.Call
import retrofit2.http.*

interface OrderDetailAPI {
    @POST("cartproduct")
    @FormUrlEncoded
    fun retrieveCart(
        @Field("username") username: String
    ): Call<List<CartProduct>>


    @POST("addqty")
    @FormUrlEncoded
    fun addqty(
        @Field("username") username: String,
        @Field("product_name") product_name: String
    ): Call<ProductMain>

    @POST("rmvqty")
    @FormUrlEncoded
    fun removeqty(
        @Field("username") username: String,
        @Field("product_name") product_name: String
    ): Call<ProductMain>

    @POST("removecartproduct")
    @FormUrlEncoded
    fun removeProduct(
        @Field("username") username: String,
        @Field("product_name") product_name: String
    ): Call<ProductMain>
}