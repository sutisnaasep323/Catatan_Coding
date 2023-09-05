package com.asep.mynoteapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.asep.mynoteapps.adapter.NoteAdapter
import com.asep.mynoteapps.databinding.ActivityMainBinding
import com.asep.mynoteapps.db.NoteHelper
import com.asep.mynoteapps.entity.Note
import com.asep.mynoteapps.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NoteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.data != null) {

            when (result.resultCode) {
                // Akan dipanggil / dijalankan jika request codenya ADD
                NoteAddUpdateActivity.RESULT_ADD -> {
                    // Ambil data, masukkan data, munculkan ke RV, tampilkan ke snackbar
                    val note = result.data?.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE) as Note
                    adapter.addItem(note)
                    binding.rvNotes.smoothScrollToPosition(adapter.itemCount - 1)
                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }
                // Akan dipanggil jika request codenya Update
                NoteAddUpdateActivity.RESULT_UPDATE -> {
                    // Ambil data dan posisi note, update data, munculkan ke RV, tampilkan ke snackbar
                    val note = result.data?.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE) as Note
                    val position = result?.data?.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0) as Int
                    adapter.updateItem(position, note) //ganti data sesuai posisi pada list
                    binding.rvNotes.smoothScrollToPosition(position)
                    showSnackbarMessage("Satu item berhasil diubah")
                }
                // Akan dipanggil jika request codenya DELETE
                NoteAddUpdateActivity.RESULT_DELETE -> {
                    // ambil posisi note, lalu hapus sesuai posisi list
                    val position = result?.data?.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0) as Int
                    adapter.removeItem(position)
                    showSnackbarMessage("Satu item berhasil dihapus")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Notes"

        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.setHasFixedSize(true)

        // Untuk mengubah catatan di RV, klik catatan yang dituju, lalu kirimkan isi catatan, dan posisi catatan
        adapter = NoteAdapter(object : NoteAdapter.OnItemClickCallback {
            override fun onItemClicked(selectedNote: Note?, position: Int?) {
                val intent = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
                intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, selectedNote)
                intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position)
                resultLauncher.launch(intent)
            }
        })
        binding.rvNotes.adapter = adapter

        // konfigurasi FAB
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
            resultLauncher.launch(intent)
        }

        //restore data
        if (savedInstanceState == null) {
            // proses ambil data
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Note>(EXTRA_STATE)
            if (list != null) {
                adapter.listNotes = list
            }
        }
    }

    // save data jika terjadi orientasi layar
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

    private fun loadNotesAsync() {
        // Mengambil data menggunakan background thread yaitu launch: async. karena kita
        // menginginkan nilai kembalian dari fungsi yang dipanggil
        lifecycleScope.launch { //lifecyclescope -> scope coroutine yang aware dengan state pada lifceycle
            binding.progressbar.visibility = View.VISIBLE //show

            // akses database
            val noteHelper = NoteHelper.getInstance(applicationContext)
            noteHelper.open()

            //GET data use background thread. dijalankan pada I/O Thread
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            binding.progressbar.visibility = View.INVISIBLE //end show

            // Menunggu sampai data diambil oleh operasi asinkron dan menetapkan hasilnya ke variabel notes
            // mendapatkan nilai kembaliannya => await()
            val notes = deferredNotes.await()
            if (notes.size > 0) { //apakah ada catatan yang tersedia? jika tidak catatan dari Array Kosong
                adapter.listNotes = notes
            } else {
                adapter.listNotes = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }

            noteHelper.close() //close agar tidak terjadi memory leak / kebocoran memory
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvNotes, message, Snackbar.LENGTH_SHORT).show()
    }
}