package com.example.rollinginthebeef.dataclass
import android.os.Parcel
import android.os.Parcelable

data class CartItem(var name: String, var detail: String, var price: Float, var category: String, var productID: Int, var productImg: String, var product_qty: Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readFloat(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(detail)
        parcel.writeFloat(price)
        parcel.writeString(category)
        parcel.writeInt(productID)
        parcel.writeString(productImg)
        parcel.writeInt(product_qty)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}