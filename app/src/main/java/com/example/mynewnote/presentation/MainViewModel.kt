package com.example.mynewnote.presentation

import android.app.Application
import androidx.lifecycle.*
import com.example.mynewnote.data.repository.NoteListRepositoryImpl
import com.example.mynewnote.domain.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = NoteListRepositoryImpl(application)

    private val getNoteListUseCase = GetNoteListUseCase(repository)
    private val getNoteItemUseCase = GetNoteItemUseCase(repository)
    private val deleteNoteItemUseCase = DeleteNoteItemUseCase(repository)

    private val addNoteItemUseCase = AddNoteItemUseCase(repository)

    fun deleteNoteItem (noteItem: NoteItem) {
        viewModelScope.launch {
            deleteNoteItemUseCase.deleteNoteItem(noteItem)
        }
    }

    val noteList = getNoteListUseCase.getNoteList()



}