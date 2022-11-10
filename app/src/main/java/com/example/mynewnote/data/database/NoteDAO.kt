package com.example.mynewnote.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDAO {
    @Query("SELECT * FROM notes ORDER BY date DESC" )
    fun getAllNotes(): LiveData<List<NoteDBModel>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id:String): NoteDBModel

    @Query(
        "SELECT * FROM notes WHERE " +
            "header LIKE '%' || :letter || '%' OR" +
            " description LIKE '%' || :letter || '%'" +
            " ORDER BY date DESC"
    )
    fun searchNote(letter: String): LiveData<List<NoteDBModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote (note: NoteDBModel)

    @Delete
    suspend fun deleteNote (note: NoteDBModel)
}