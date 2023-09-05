package com.asep.mynoteapps.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.asep.mynoteapps.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME

/*
    Kelas ini untuk membuat database dan menghandle / konfigurasi perubahan skema database pada tabel.
    Atau yang biasa dikenal dengan DDL. seperti yang terjadi pada onUpgrade (Add, Delete, Edit)
    sehingga akses nama tabel dan nama kolom tabel menjadi lebih mudah
 */
internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbnoteapp"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.NoteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.NoteColumns.TITLE} TEXT NOT NULL," +
                " ${DatabaseContract.NoteColumns.DESCRIPTION} TEXT NOT NULL," +
                " ${DatabaseContract.NoteColumns.DATE} TEXT NOT NULL)"
    }

    // Create Database first
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    // Konfigurasi Database, delete lalu buat ulang database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME") // delete TABLE NAME (yang sudah ada)
        onCreate(db) // create new database (perbarui database) dengan new version
    }
}