package com.example.rollinginthebeef.dataclass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReceiptImage(

    @Expose
    @SerializedName("receipt_img") var receipt_image: String

) {
}