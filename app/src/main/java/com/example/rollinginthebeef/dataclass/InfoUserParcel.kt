package com.example.rollinginthebeef.dataclass

import android.os.Parcel
import android.os.Parcelable

data class infoUserParcel(val userID: Int, val userName: String, val userTel: String, val userAddress: String, val userEmail: String, val nameUser: String) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userID)
        parcel.writeString(userName)
        parcel.writeString(userTel)
        parcel.writeString(userAddress)
        parcel.writeString(userEmail)
        parcel.writeString(nameUser)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<infoUserParcel> {
        override fun createFromParcel(parcel: Parcel): infoUserParcel {
            return infoUserParcel(parcel)
        }

        override fun newArray(size: Int): Array<infoUserParcel?> {
            return arrayOfNulls(size)
        }
    }
}