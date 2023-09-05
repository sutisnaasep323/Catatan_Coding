package com.asep.mynoteapps.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
    Mengolah data menggunakan model class karena lebih simple dibanding objek Cursor
    lalu untuk mempermudah pengiriman data
 */

@Parcelize
data class Note(
    var id: Int = 0,
    var title: String? = null,
    var description: String? = null,
    var date: String? = null
): Parcelable

