package com.example.mynewnote.domain

class AddNoteItemUseCase(private val noteListRepository: NoteListRepository) {
    fun addNoteItem (noteItem: NoteItem) {
noteListRepository.addNoteItem(noteItem)
    }
}