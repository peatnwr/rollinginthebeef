package com.example.rollinginthebeef.retrofits

import com.example.rollinginthebeef.modules.changePasswordUser
import com.example.rollinginthebeef.modules.loginUser
import com.example.rollinginthebeef.modules.registerUser
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
    ):Call<registerUser>

    @POST("login")
    @FormUrlEncoded
    fun loginUser(
        @Field("user_username") user_username: String,
        @Field("user_password") user_password: String
    ):Call<loginUser>

    @PATCH("changepassword")
    @FormUrlEncoded
    fun changePasswordUser(
        @Field("user_username") user_username: String,
        @Field("user_password") user_password: String,
        @Field("user_cfpassword") user_cfpassword: String
    ):Call<changePasswordUser>

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
    ): Call<loginUser>

    @DELETE("userpermission/{user_id}")
    fun deletePermissionUser(
        @Path("user_id") user_id: Int
    ): Call<loginUser>

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
    ): Call<loginUser>
}