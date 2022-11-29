package com.example.mynewnote.domain

class SearchNoteItemUseCase(private val noteListRepository: NoteListRepository) {
    fun searchNoteList (letter: String): List<NoteItem> {
        return noteListRepository.searchNoteLst(letter)
    }
}