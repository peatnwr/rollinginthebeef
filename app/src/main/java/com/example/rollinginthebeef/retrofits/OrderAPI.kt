package com.example.rollinginthebeef.retrofits

import com.example.rollinginthebeef.dataclass.OrderHistory
import com.example.rollinginthebeef.dataclass.OrderID
import com.example.rollinginthebeef.dataclass.ReceiptImage
import retrofit2.Call
import retrofit2.http.*

interface OrderAPI {

    @POST("confirmorder")
    @FormUrlEncoded
    fun confirmOrder(
        @Field("username") username: String,
        @Field("order_date") order_date: String,
        @Field("order_time") order_time: String,
        @Field("order_total") order_total: Double
    ):Call<OrderID>

    @GET("getorderid/{username}")
    fun getOrderId(
        @Path("username") username: String
    ): Call<OrderID>

    @PUT("addreceipt/{order_id}")
    @FormUrlEncoded
    fun uploadImage(
        @Path("order_id")order_id: Int,
        @Field("img_name") img_name: String,
        @Field("tranfer_time") tranfer_time: String
    ):Call<OrderID>

    @PUT("cancelorder/{order_id}")
    fun cancelOrder(
        @Path("order_id") order_id: Int
    ):Call<OrderID>

    @GET("getorderhistory/{user_name}")
    fun getOrderHistory(
        @Path("user_name") user_name: String
    ):Call<List<OrderHistory>>

    @GET("getalluserorder/{user_name}")
    fun getAllUserOrder(
        @Path("user_name") user_name:String
    ):Call<List<OrderHistory>>

    @GET("activeorder/{user_name}")
    fun getActiveOrder(
        @Path("user_name") user_name: String
    ):Call<OrderHistory>

    @GET("receiptimage/{order_id}")
    fun getReceiptImage(
        @Path("order_id") order_id: Int
    ):Call<ReceiptImage>
}


