package com.example.mynewnote.domain

class SearchNoteItemUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun searchNoteList(newText: String): List<NoteItem> {
        return noteListRepository.searchNoteLst(newText)
    }
}