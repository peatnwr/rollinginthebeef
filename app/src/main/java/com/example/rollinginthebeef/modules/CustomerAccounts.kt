package com.example.rollinginthebeef.modules

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CustomerAccounts(
    @Expose
    @SerializedName("user_name") val user_name: String,

    @Expose
    @SerializedName("user_tel") val user_tel: String,

    @Expose
    @SerializedName("user_email") val user_email: String,

    @Expose
    @SerializedName("user_address") val user_address: String,

    @Expose
    @SerializedName("user_id") val user_id: Int) {}