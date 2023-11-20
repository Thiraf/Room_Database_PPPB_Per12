package com.example.tugasasdos12

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao

interface NoteDao {
//    Fungsi ini digunakan untuk menyisipkan (insert) data baru ke dalam database.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
// menunjukkan bahwa jika terjadi konflik, data yang baru akan diabaikan (tidak ada tindakan khusus yang diambil).
    fun insert(note: Note)

//    Fungsi ini digunakan untuk memperbarui data yang sudah ada dalam database.
    @Update
    fun update(note: Note)

//    Fungsi ini digunakan untuk menghapus data dari database.
    @Delete
    fun delete(note: Note)


//Fungsi ini digunakan untuk mengambil semua catatan (notes) dari tabel note_table dan mengembalikannya sebagai objek LiveData<List<Note>>.
    @get:Query("SELECT * from note_table ORDER BY id ASC")
    val  allNotes:LiveData<List<Note>>
}