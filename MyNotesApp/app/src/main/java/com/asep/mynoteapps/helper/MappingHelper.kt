package com.asep.mynoteapps.helper

import android.database.Cursor
import com.asep.mynoteapps.db.DatabaseContract
import com.asep.mynoteapps.entity.Note

/*
    Kelas untuk engonversi dari Cursor ke Arraylist. kenapa perlu konversi? karena  di adapter kita
    menggunakan arraylist, sedangkan di sini objek yang di kembalikan berupa Cursor

    Kelas ini nantinya untuk mengambil data menggunakan method queryAll()
 */

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Note> {
        val notesList = ArrayList<Note>()
        notesCursor?.apply { //tanpa apply kode menjadi: notesCursor.geInt dan notesCursor.getColumnIndexOrThrow
            while (moveToNext()) { //moveToNext digunakan untuk memindahkan cursor ke baris selanjutnya
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE))
                val description = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE))
                notesList.add(Note(id, title, description, date))
            }
        }
        return notesList
    }
}