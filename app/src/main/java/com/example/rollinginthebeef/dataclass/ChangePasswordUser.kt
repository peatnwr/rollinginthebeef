package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class changePasswordUser(
    @Expose
    @SerializedName("user_username") val user_username: String,

    @Expose
    @SerializedName("user_password") val user_password: String,

    @Expose
    @SerializedName("user_cfpassword") val user_cfpassword: String) {}