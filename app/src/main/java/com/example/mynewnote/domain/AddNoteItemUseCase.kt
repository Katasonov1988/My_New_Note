package com.example.mynewnote.domain

class AddNoteItemUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun addNoteItem(noteItem: NoteItem) {
        noteListRepository.addNoteItem(noteItem)
    }
}