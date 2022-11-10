package com.example.mynewnote.domain

import android.provider.ContactsContract
import androidx.lifecycle.LiveData

class GetNoteItemUseCase(private val noteListRepository: NoteListRepository) {
   suspend fun getNoteItem (noteItemId: String): NoteItem  {
       return noteListRepository.getNoteItem(noteItemId)
    }
}