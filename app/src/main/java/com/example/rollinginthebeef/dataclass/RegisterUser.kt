package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterUser(
    @Expose
    @SerializedName("user_username") val user_username: String,

    @Expose
    @SerializedName("user_password") val user_password: String,

    @Expose
    @SerializedName("user_tel") val user_tel: String,

    @Expose
    @SerializedName("user_address") val user_address: String,

    @Expose
    @SerializedName("user_email") val user_email: String,

    @Expose
    @SerializedName("user_name") val user_name: String) {}