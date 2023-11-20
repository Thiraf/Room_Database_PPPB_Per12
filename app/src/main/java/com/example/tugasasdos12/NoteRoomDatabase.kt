package com.example.tugasasdos12

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

//Ini untuk menginisialisasi DB. entitas digunakan untuk menetapkam isi
@Database(entities = [Note::class],
    version = 1,
    exportSchema = false
)

abstract class NoteRoomDatabase: RoomDatabase() {
//    Ini digunakan untuk mendapatkan objek NoteDao, yang berisi metode untuk melakukan operasi database terkait Note.
    abstract fun noteDao():NoteDao?

    companion object{
//        @Volatile menunjukkan bahwa nilai INSTANCE akan selalu diperbarui dari memori utama, sehingga setiap perubahan akan terlihat oleh semua thread.
        @Volatile
        private var INSTANCE : NoteRoomDatabase ? = null
//        getDatabase adalah metode statis yang digunakan untuk mendapatkan instance dari database. Jika INSTANCE masih null, maka database akan dibuat menggunakan databaseBuilder.
        fun getDatabase(context: Context):NoteRoomDatabase?{
            if(INSTANCE == null){
                synchronized(NoteRoomDatabase::class.java){
                    INSTANCE = databaseBuilder(context.applicationContext,
                        NoteRoomDatabase::class.java,"note_database").build()
                }
            }
            return INSTANCE
        }
    }
}