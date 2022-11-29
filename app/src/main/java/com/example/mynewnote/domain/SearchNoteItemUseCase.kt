package com.example.mynewnote.domain

import androidx.lifecycle.LiveData

class SearchNoteItemUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun searchNoteList (newText: String):  List<NoteItem>  {
        return noteListRepository.searchNoteLst(newText)
    }
}