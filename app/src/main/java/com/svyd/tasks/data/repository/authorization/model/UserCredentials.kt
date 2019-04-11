package com.svyd.tasks.data.repository.authorization.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

import com.google.gson.annotations.SerializedName

class UserCredentials() : Parcelable {

    @SerializedName("email")
    var email: String? = null

    @SerializedName("password")
    var password: String? = null

    constructor(parcel: Parcel) : this() {
        email = parcel.readString()
        password = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<UserCredentials> {
        override fun createFromParcel(parcel: Parcel): UserCredentials {
            return UserCredentials(parcel)
        }

        override fun newArray(size: Int): Array<UserCredentials?> {
            return arrayOfNulls(size)
        }
    }
}