package com.asep.mynoteapps.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.asep.mynoteapps.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.asep.mynoteapps.db.DatabaseContract.NoteColumns.Companion._ID
import java.sql.SQLException

/*
    Kelas ini untuk mengakomodasi kebutuhan DML, yaitu tools untuk manipulasi isi data pada database
 */

class NoteHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        // Menginisiasi database dengan  Singleton Pattern Sehingga tidak terjadi duplikasi instance
        private var INSTANCE: NoteHelper? = null
        fun getInstance(context: Context): NoteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NoteHelper(context)
            }
    }

    // Membuka koneksi database
    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    // Menutup koneksi database
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    //mengambil semua data pada database secara ASC (A-Z)
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    //mengambil data menggunakan ID
    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    // Untuk menyimpan isi data ke table database
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    // Untuk memperbarui isi data ke table database
    fun update(id: String, values: ContentValues?): Int {
        // tanda tanya digantikan dengan arrayOf(id) untuk pengamanan
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    // Untuk menghapus isi catatan dari database
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = ?", arrayOf(id))
//        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null) //cara kedua. baiknya cara pertama
    }
}