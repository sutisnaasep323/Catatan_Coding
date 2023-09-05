package com.asep.mynoteapps.db

import android.provider.BaseColumns

/*
    Kelas ini Mempermudah Akses nama Tabel dan Kolom pada Database
 */

internal class DatabaseContract {
    internal class NoteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "note"

            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"
        }
    }
}