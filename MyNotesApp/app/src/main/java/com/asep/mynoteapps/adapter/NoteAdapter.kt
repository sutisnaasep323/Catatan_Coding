package com.asep.mynoteapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asep.mynoteapps.R
import com.asep.mynoteapps.databinding.ItemNoteBinding
import com.asep.mynoteapps.entity.Note

/*
    Membuat adapter ditambah constructor OnItemClickCallback karnea kita ingin membaca hasil
    klik pada item RecyclerView di MainActivity
 */
class NoteAdapter(private val onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    // Menyimpan nilai/data Item Catatan menggunakan ArrayList
    var listNotes = ArrayList<Note>()
        set(listNotes) { //setter
            if (listNotes.size > 0) { //membersihkan data
                this.listNotes.clear()
            }
            this.listNotes.addAll(listNotes)
        }

    // Menambahkan item/Catatan baru di listnote
    fun addItem(note: Note) {
        this.listNotes.add(note)
        //memberitahukan adapter bahwa item baru telah ditambahkan, sehingga RecyclerView dapat diperbarui dengan menampilkan item yang baru dengan animasi
        notifyItemInserted(this.listNotes.size - 1)
    }

    // Memperbarui Item listnote
    fun updateItem(position: Int, note: Note) {
        this.listNotes[position] = note
        notifyItemChanged(position, note) //animasi
    }

    // Menghapus Item listnote
    fun removeItem(position: Int) {
        this.listNotes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNotes.size)
    }

    //mengatur tampilan item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    //mengatur data catatan ke tampilan item catatan yang sesuai.
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    // mengembalikan jumlah item yang ada dalam daftar catatan (listNotes).
    override fun getItemCount(): Int = this.listNotes.size

    // untuk menangani tampilan item catatan pada RecyclerView
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemNoteBinding.bind(itemView)
        fun bind(note: Note) {
            binding.tvItemTitle.text = note.title
            binding.tvItemDate.text = note.date
            binding.tvItemDescription.text = note.description
            binding.cvItemNote.setOnClickListener {
                onItemClickCallback.onItemClicked(note, adapterPosition)
            }
        }
    }

    // callback untuk menangani peristiwa klik pada catatan yang dipilih
    interface OnItemClickCallback {
        fun onItemClicked(selectedNote: Note?, position: Int?)
    }
}