package com.example.mynewnote.data.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteDBModel::class], version = 1, exportSchema = false)
abstract class NotesDataBase: RoomDatabase() {
    companion object {
        private var db: NotesDataBase? = null
        private const val DB_NAME = "main.db"
        private val LOCK = Any()

        fun getInstance(application: Application): NotesDataBase {
            synchronized(LOCK) {
                db?.let { return it }

                val instance =
                    Room.databaseBuilder(
                        application,
                        NotesDataBase::class.java,
                        DB_NAME
                    ).build()
                db = instance
                return instance
            }
        }
    }
    abstract fun noteDao(): NoteDAO
}