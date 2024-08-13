package com.example.myapplication.dataModels.Spotify

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyTrack(
    @SerializedName("id") var trackId: String,
    @SerializedName("name") var name: String
) : Serializable,Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackId)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyTrack> {
        override fun createFromParcel(parcel: Parcel): MyTrack {
            return MyTrack(parcel)
        }

        override fun newArray(size: Int): Array<MyTrack?> {
            return arrayOfNulls(size)
        }
    }
}

