package com.example.mynewnote.domain

interface NoteListRepository  {

    fun addNoteItem (noteItem: NoteItem)

    fun deleteNoteItem (noteItem: NoteItem)

    fun editNoteItem (noteItem: NoteItem)

    fun getNoteItem (noteItemId: Int): NoteItem

    fun getNoteList(): List<NoteItem>

    fun searchNoteLst (letter: String): List<NoteItem>
}