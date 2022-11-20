package com.example.mynewnote.presentation.firsts_creen

import android.app.Application
import androidx.lifecycle.*
import com.example.mynewnote.data.repository.NoteListRepositoryImpl
import com.example.mynewnote.domain.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = NoteListRepositoryImpl(application)

    private val getNoteListUseCase = GetNoteListUseCase(repository)
    private val deleteNoteItemUseCase = DeleteNoteItemUseCase(repository)

    fun deleteNoteItem (noteItem: NoteItem) {
        viewModelScope.launch {
            deleteNoteItemUseCase.deleteNoteItem(noteItem)
        }
    }

    val noteList = getNoteListUseCase.getNoteList()



}