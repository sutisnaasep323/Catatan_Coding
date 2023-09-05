package com.asep.mynoteapps

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.asep.mynoteapps.databinding.ActivityNoteAddUpdateBinding
import com.asep.mynoteapps.db.DatabaseContract
import com.asep.mynoteapps.db.NoteHelper
import com.asep.mynoteapps.entity.Note
import java.text.SimpleDateFormat
import java.util.*

class NoteAddUpdateActivity : AppCompatActivity(), View.OnClickListener {
    private var isEdit = false
    private var note: Note? = null
    private var position: Int = 0
    private lateinit var noteHelper: NoteHelper

    private lateinit var binding: ActivityNoteAddUpdateBinding

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 101
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inisialisasi database
        noteHelper = NoteHelper.getInstance(applicationContext)
        noteHelper.open() //open koneksi database

        //mengambil note dari intent
        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) { //jika ternyata terdapat data yang dikirimkan (Update Note)
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true //change name button jika ada data yang dikirimkan
        } else { //jika tidak ada data yang dikirim (New Note)
            note = Note()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) { // jika ada data yang dikirimkan
            actionBarTitle = "Ubah"
            btnTitle = "Update"

            note?.let {
                binding.edtTitle.setText(it.title)
                binding.edtDescription.setText(it.description)
            }

        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnSubmit.text = btnTitle

        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_submit) {

            //ambil data
            val title = binding.edtTitle.text.toString().trim()
            val description = binding.edtDescription.text.toString().trim()

            if (title.isEmpty()) {
                binding.edtTitle.error = "Field can not be blank"
                return
            }

            //simpan data
            note?.title = title
            note?.description = description

            //kirim data catatan
            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)

            //kirim/tulis data ke database
            val values = ContentValues()
            values.put(DatabaseContract.NoteColumns.TITLE, title)
            values.put(DatabaseContract.NoteColumns.DESCRIPTION, description)

            //jika ada data, isi data secara otomatis
            if (isEdit) {
                //update data kembalikan nilai RESULT UPDATE jika berhasil
                val result = noteHelper.update(note?.id.toString(), values)
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(this@NoteAddUpdateActivity, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
                }
            } else { //jika tidak ada data maka buat data/catatan baru
                note?.date = getCurrentDate() //tambah tanggal dan jam
                values.put(DatabaseContract.NoteColumns.DATE, getCurrentDate()) //kirim tanggalnya ke database
                val result = noteHelper.insert(values) //simpan 3 data ke dalam database

                //kembalikan hasil RESULT ADD
                if (result > 0) {
                    note?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@NoteAddUpdateActivity, "Gagal menambah data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    //membuat MENU
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE) //kenapa PERLU PANGGIL LAGI?
        }
        return super.onOptionsItemSelected(item)
    }

    //jika kembali ke activity sebelumnya maka tampilkan dialog
    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    // Tampilan dialog untuk back and delete
    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) { //jika back activity
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else { //jika delete
            dialogTitle = "Hapus Note"
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                if (isDialogClose) { //jika back activity maka tombol Ya nya berisi
                    finish()
                } else { //jika delete maka tombol Ya nya berisi
                    val result = noteHelper.deleteById(note?.id.toString()).toLong() //hapus data

                    //kembalikan hasil RESULT
                    if (result > 0) {
                        // berhasil RESULT delete untuk dikirim ke MainActivity
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@NoteAddUpdateActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}