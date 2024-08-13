package com.example.myapplication.dataModels.Spotify

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MyPlaylists(
    @SerializedName("items") var items: List<PlayList>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(PlayList.CREATOR) ?: ArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyPlaylists> {
        override fun createFromParcel(parcel: Parcel): MyPlaylists {
            return MyPlaylists(parcel)
        }

        override fun newArray(size: Int): Array<MyPlaylists?> {
            return arrayOfNulls(size)
        }
    }
}
