package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Category (
    @Expose
    @SerializedName("category_id") val category_id: Int,

    @Expose
    @SerializedName("category_name") val category_name: String
) {
}