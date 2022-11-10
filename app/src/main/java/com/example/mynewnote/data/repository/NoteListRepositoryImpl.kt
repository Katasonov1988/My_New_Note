package com.example.mynewnote.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mynewnote.data.database.NotesDataBase
import com.example.mynewnote.data.mapper.NoteMapper
import com.example.mynewnote.domain.NoteItem
import com.example.mynewnote.domain.NoteListRepository
import kotlinx.coroutines.coroutineScope

class NoteListRepositoryImpl(
    private val application: Application
): NoteListRepository {

    private val noteDao = NotesDataBase.getInstance(application).noteDao()
    private val mapper = NoteMapper()



    override suspend fun addNoteItem(noteItem: NoteItem) {
        noteDao.insertNote(mapper.mapEntityToDBModel(noteItem))
    }

    override suspend fun deleteNoteItem(noteItem: NoteItem) {
        noteDao.deleteNote(mapper.mapEntityToDBModel(noteItem))
    }

    override suspend fun editNoteItem(noteItem: NoteItem) {
        noteDao.insertNote(mapper.mapEntityToDBModel(noteItem))
    }

    override suspend fun getNoteItem(noteItemId: String): NoteItem  {
        val dbModel = noteDao.getNoteById(noteItemId)
            return mapper.mapDBModelToEntity(dbModel)

    }

    override fun getNoteList(): LiveData<List<NoteItem>>  {
        return Transformations.map(noteDao.getAllNotes()) {
            it.map {
                mapper.mapDBModelToEntity(it)
            }
        }
    }

    override fun searchNoteLst(letter: String): LiveData<List<NoteItem>>  {
        return  Transformations.map(noteDao.searchNote(letter)) {
            it.map {
                mapper.mapDBModelToEntity(it)
            }
        }
    }
}