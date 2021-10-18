package com.example.rollinginthebeef.retrofits

import com.example.rollinginthebeef.dataclass.*
import retrofit2.Call
import retrofit2.http.*

interface ProductAPI {
    @GET("allproduct")
    fun retrieveProduct(): Call<List<ProductMain>>

    @GET("editproductadmin/{product_name}")
    fun editProduct(
        @Path("product_name") product_name: String
    ): Call<ProductList>

    @GET("allcategory")
    fun retrieveCategory(): Call<List<Category>>

    @POST("cartproduct")
    @FormUrlEncoded
    fun retrieveCart(
        @Field("order_id") order_id: Int
    ): Call<List<CartProduct>>

    @GET("receiptproduct/{order_id}")
    fun receiptProduct(
        @Path("order_id") order_id: Int
    ): Call<List<ReceiptProduct>>

    @GET("receiptdetail/{order_id}")
    fun receiptDetail(
        @Path("order_id") order_id: Int
    ): Call<ReceiptDetail>

}