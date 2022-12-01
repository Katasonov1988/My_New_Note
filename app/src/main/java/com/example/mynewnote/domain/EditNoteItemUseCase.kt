package com.example.mynewnote.domain

class EditNoteItemUseCase(private val noteListRepository: NoteListRepository) {
    suspend fun editNoteItem(noteItem: NoteItem) {
        noteListRepository.editNoteItem(noteItem)

    }
}