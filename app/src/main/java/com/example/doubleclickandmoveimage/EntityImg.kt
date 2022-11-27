package com.example.doubleclickandmoveimage

import android.os.Parcel
import android.os.Parcelable

data class EntityImg(
    var colorX: Float = 0.0f,
    var colorY: Float = 0.0f,
    var bwX: Float = 0.0f,
    var bwY: Float = 0.0f,

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(colorX)
        parcel.writeFloat(colorY)
        parcel.writeFloat(bwX)
        parcel.writeFloat(bwY)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntityImg> {
        override fun createFromParcel(parcel: Parcel): EntityImg {
            return EntityImg(parcel)
        }

        override fun newArray(size: Int): Array<EntityImg?> {
            return arrayOfNulls(size)
        }
    }
}
