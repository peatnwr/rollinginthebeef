package com.example.rollinginthebeef.retrofits

import com.example.rollinginthebeef.modules.*
import retrofit2.Call
import retrofit2.http.*

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

    @POST("addproduct")
    @FormUrlEncoded
    fun addProduct(
        @Field("product_name") product_name: String,
        @Field("product_price") product_price: Int,
        @Field("product_detail") product_detail: String,
        @Field("product_img") product_img: String,
        @Field("product_qty") product_qty: Int,
        @Field("category_id") category_id: Int
    ): Call<ProductList>

    @GET("userpermission")
    fun retrieveUser(): Call<List<UserPermission>>

    @GET("customeraccounts")
    fun retrieveCustomer(): Call<List<CustomerAccounts>>
}