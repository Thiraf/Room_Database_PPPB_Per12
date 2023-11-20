package com.example.tugasasdos12

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//menandakan bahwa kelas Note adalah entitas yang akan dihubungkan ke tabel dalam database
@Entity(tableName = "note_table")
data class Note (
//    menandakan bahwa properti id adalah kunci utama (primary key) dari tabel
//    menunjukkan bahwa nilai kunci utama akan dihasilkan secara otomatis oleh database saat sebuah catatan baru ditambahkan.
    @PrimaryKey(autoGenerate = true)
//    id tidak boleh kosong
    @NonNull
    val id:Int = 0,

    @ColumnInfo(name= "title")
    val title : String,

    @ColumnInfo(name = "description")
    val description : String
)