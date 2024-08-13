package com.example.myapplication.dataModels.Spotify

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Tracks(
    @SerializedName("total") var total: Int,
    @SerializedName("items") var items: List<Item>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(Item.CREATOR) ?: listOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(total)
        parcel.writeTypedList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tracks> {
        override fun createFromParcel(parcel: Parcel): Tracks {
            return Tracks(parcel)
        }

        override fun newArray(size: Int): Array<Tracks?> {
            return arrayOfNulls(size)
        }
    }
}


