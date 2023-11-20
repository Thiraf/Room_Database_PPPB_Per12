package com.example.tugasasdos12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.tugasasdos12.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// MainActivity adalah kelas utama yang mewakili antarmuka pengguna utama.
class MainActivity : AppCompatActivity() {

    // Membuat objek NoteDao untuk berinteraksi dengan database.
    private lateinit var mNotesDao: NoteDao

    // ExecutorService digunakan untuk menjalankan operasi database pada thread terpisah.
    private lateinit var executorService: ExecutorService

    // updateId digunakan untuk menyimpan ID catatan yang akan diperbarui atau dihapus.
    private var updateId: Int = 0

    // Binding untuk merujuk ke elemen-elemen UI menggunakan View Binding.
    private lateinit var binding: ActivityMainBinding

    // Metode onCreate() akan dijalankan saat aktivitas dibuat.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi View Binding.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ExecutorService untuk thread terpisah.
        executorService = Executors.newSingleThreadExecutor()

        // Mendapatkan instance dari database dan inisialisasi NoteDao.
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        // Mengatur event listener untuk tombol tambah catatan.
        binding.btnAdd.setOnClickListener {
            // Menambahkan catatan baru ke database.
            insert(
                Note(
                    title = binding.edtTitle.text.toString(),
                    description = binding.edtDry.text.toString()
                )
            )
            // Mengosongkan field setelah menambahkan catatan.
            setEmptyField()
        }

        // Mengatur event listener untuk item yang dipilih dalam ListView.
        binding.listView.setOnItemClickListener { adapterView, _, i, _ ->
            // Mendapatkan item catatan yang dipilih.
            val item = adapterView.adapter.getItem(i) as Note
            // Menyimpan ID catatan yang akan diperbarui atau dihapus.
            updateId = item.id
            // Menampilkan judul dan deskripsi catatan yang dipilih.
            binding.edtTitle.setText(item.title)
            binding.edtDry.setText(item.description)
        }

        // Mengatur event listener untuk tombol update catatan.
        binding.btnUpdate.setOnClickListener {
            // Memperbarui catatan dengan data yang baru.
            update(
                Note(
                    id = updateId,
                    title = binding.edtTitle.text.toString(),
                    description = binding.edtDry.text.toString()
                )
            )
            // Mengosongkan field setelah memperbarui catatan.
            updateId = 0
            setEmptyField()
        }

        // Mengatur event listener untuk tombol hapus catatan.
        binding.btnDelete.setOnClickListener {
            // Memeriksa apakah ada catatan yang akan dihapus.
            if (updateId != 0) {
                // Membuat objek catatan dengan ID yang akan dihapus.
                val noteToDelete = Note(id = updateId, title = "", description = "")
                // Menghapus catatan dari database.
                delete(noteToDelete)
                // Mengosongkan field setelah menghapus catatan.
                updateId = 0
                setEmptyField()
            }
        }
    }

    // Metode untuk mengosongkan field judul dan deskripsi.
    private fun setEmptyField(){
        with(binding){this@MainActivity
            edtTitle.setText("")
            edtDry.setText("")
        }
    }

    // Metode untuk mengambil semua catatan dari database dan menampilkannya di ListView.
    private fun getAllNotes() {
        // Mengamati perubahan pada LiveData yang mengembalikan semua catatan.
        mNotesDao.allNotes.observe(this) { notes ->
            // Membuat adapter ArrayAdapter untuk ListView.
            val adapter: ArrayAdapter<Note> =
                ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, notes)
            // Mengatur adapter ke ListView.
            binding.listView.adapter = adapter
        }
    }

    // Metode onResume() dipanggil saat aktivitas dilanjutkan (resumed).
    override fun onResume() {
        super.onResume()
        // Memanggil metode untuk mendapatkan semua catatan saat aktivitas dilanjutkan.
        getAllNotes()
    }

    // Metode untuk menambahkan catatan baru ke database.
    private fun insert(note: Note) {
        // Menjalankan operasi insert pada thread terpisah.
        executorService.execute {
            mNotesDao.insert(note)
        }
    }

    // Metode untuk memperbarui catatan dalam database.
    private fun update(note: Note) {
        // Menjalankan operasi update pada thread terpisah.
        executorService.execute {
            mNotesDao.update(note)
        }
    }

    // Metode untuk menghapus catatan dari database.
    private fun delete(note: Note) {
        // Menjalankan operasi delete pada thread terpisah.
        executorService.execute {
            mNotesDao.delete(note)
        }
    }
}
