package com.example.mynewnote.domain

import android.provider.ContactsContract

class GetNoteItemUseCase(private val noteListRepository: NoteListRepository) {
    fun getNoteItem (noteItemId: Int): NoteItem {
       return noteListRepository.getNoteItem(noteItemId)
    }
}