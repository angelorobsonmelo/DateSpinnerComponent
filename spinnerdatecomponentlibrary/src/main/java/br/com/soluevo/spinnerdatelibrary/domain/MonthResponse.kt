package br.com.soluevo.spinnerdatelibrary.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MonthResponse(
    var id: String,
    var month: Int,
    var year: Int,
    @Transient
    var title: String
) : Parcelable {
    constructor(month: Int, year: Int) : this("", month, year, "")
}