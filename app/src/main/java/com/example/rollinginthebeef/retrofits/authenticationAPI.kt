package com.example.rollinginthebeef.retrofits

import com.example.rollinginthebeef.dataclass.*
import retrofit2.Call
import retrofit2.http.*

interface authenticationAPI {
    @POST("register")
    @FormUrlEncoded
    fun registerUser(
        @Field("user_username") user_username: String,
        @Field("user_password") user_password: String,
        @Field("user_tel") user_tel: String,
        @Field("user_address") user_address: String,
        @Field("user_email") user_email: String,
        @Field("user_name") user_name: String
    ):Call<RegisterUser>

    @POST("login")
    @FormUrlEncoded
    fun loginUser(
        @Field("user_username") user_username: String,
        @Field("user_password") user_password: String
    ):Call<LoginUser>

    @PATCH("changepassword")
    @FormUrlEncoded
    fun changePasswordUser(
        @Field("user_username") user_username: String,
        @Field("user_password") user_password: String,
        @Field("user_cfpassword") user_cfpassword: String
    ):Call<changePasswordUser>

    @POST("addorder")
    @FormUrlEncoded
    fun addOrder(
        @Field("username") username: String,
        @Field("product_id") product_id: Int,
        @Field("product_price") product_price: Float
    ): Call<OrderID>

    @POST("createorder")
    @FormUrlEncoded
    fun createOrder(
        @Field("username") username: String,
    ): Call<OrderID>

    @GET("getuserdata/{username}")
    fun getUserData(
        @Path("username") username: String
    ): Call<CustomerAccounts>

    @PUT("edituserprofile/{username}")
    @FormUrlEncoded
    fun editUserProfile(
        @Path("username") username : String,
        @Field("name") name: String,
        @Field("tel") tel: String,
        @Field("email") email: String,
        @Field("address") address: String,
        @Field("password") password: String,
        @Field("confirm") confirm: String
    ): Call<LoginUser>

    @PATCH("editprofileadmin")
    @FormUrlEncoded
    fun editProfileAdmin(
        @Field("user_id") user_id: Int,
        @Field("user_name") user_name: String,
        @Field("user_email") user_email: String,
        @Field("user_tel") user_tel: String,
        @Field("user_address") user_address: String,
        @Field("user_username") user_username: String,
        @Field("user_password") user_password: String,
        @Field("user_cfpassword") user_cfpassword: String
    ): Call<LoginUser>

    @DELETE("userpermission/{user_id}")
    fun deletePermissionUser(
        @Path("user_id") user_id: Int
    ): Call<LoginUser>

    @DELETE("customeraccounts/{user_id}")
    fun deleteCustomer(
        @Path("user_id") user_id: Int
    ): Call<LoginUser>

    @POST("addstaff")
    @FormUrlEncoded
    fun addStaff(
        @Field("user_name") user_name: String,
        @Field("user_email") user_email: String,
        @Field("user_tel") user_tel: String,
        @Field("user_address") user_address: String,
        @Field("user_username") user_username: String,
        @Field("user_type") user_type: Int,
        @Field("user_password") user_password: String,
        @Field("user_cfpassword") user_cfpassword: String
    ): Call<LoginUser>
}