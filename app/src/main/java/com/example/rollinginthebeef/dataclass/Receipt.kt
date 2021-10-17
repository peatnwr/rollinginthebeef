package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Receipt (

    @Expose
    @SerializedName("order_id") val order_id: Int){}