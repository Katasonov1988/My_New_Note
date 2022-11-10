package com.example.mynewnote.domain

import androidx.lifecycle.LiveData

interface NoteListRepository  {

    suspend fun addNoteItem (noteItem: NoteItem)
    suspend fun deleteNoteItem (noteItem: NoteItem)
    suspend fun editNoteItem (noteItem: NoteItem)
    suspend fun getNoteItem (noteItemId: String): NoteItem
    fun getNoteList(): LiveData<List<NoteItem>>
    fun searchNoteLst (letter: String): LiveData<List<NoteItem>>
}