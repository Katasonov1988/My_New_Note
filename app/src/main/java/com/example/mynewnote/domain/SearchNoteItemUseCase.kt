package com.example.mynewnote.domain

import androidx.lifecycle.LiveData

class SearchNoteItemUseCase(private val noteListRepository: NoteListRepository) {
    fun searchNoteList (letter: String): LiveData<List<NoteItem>>  {
        return noteListRepository.searchNoteLst(letter)
    }
}