package com.example.myapplication.dataModels.Spotify

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PlayList(
    @SerializedName("tracks") var tracks: Tracks,
    @SerializedName("name") var name: String,
    @SerializedName("images") var images: List<ImageObject>,
    @SerializedName("id") var spotifyId: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Tracks::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(ImageObject.CREATOR)!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(tracks, flags)
        parcel.writeString(name)
        parcel.writeTypedList(images)
        parcel.writeString(spotifyId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayList> {
        override fun createFromParcel(parcel: Parcel): PlayList {
            return PlayList(parcel)
        }

        override fun newArray(size: Int): Array<PlayList?> {
            return arrayOfNulls(size)
        }
    }
}