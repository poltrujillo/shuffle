package com.example.myapplication.dataModels.Spotify

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ImageObject(
    @SerializedName("url") var url: String,
    @SerializedName("height") var height: Int,
    @SerializedName("width") var width: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeInt(height)
        parcel.writeInt(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageObject> {
        override fun createFromParcel(parcel: Parcel): ImageObject {
            return ImageObject(parcel)
        }

        override fun newArray(size: Int): Array<ImageObject?> {
            return arrayOfNulls(size)
        }
    }
}
