package com.example.mynewnote.domain

class DeleteNoteItemUseCase(private val noteListRepository: NoteListRepository) {
   suspend fun deleteNoteItem (noteItem: NoteItem) {
        noteListRepository.deleteNoteItem(noteItem)
    }
}