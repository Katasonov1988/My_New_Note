package com.example.mynewnote.presentation.search_noteitem_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mynewnote.data.repository.NoteListRepositoryImpl
import com.example.mynewnote.domain.NoteItem
import com.example.mynewnote.domain.SearchNoteItemUseCase
import kotlinx.coroutines.launch

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val repository = NoteListRepositoryImpl(application)

    private val searchNoteItemUseCase = SearchNoteItemUseCase(repository)

//fun searchNotes (newText: String): List<NoteItem> {
//    val searchedItems = searchNoteItemUseCase.searchNoteList(newText)
//return searchedItems.
//
//}
    private var _ldListNotes = MutableLiveData<List<NoteItem>>()
    val ldListNotes: LiveData<List<NoteItem>>
    get() = _ldListNotes



    fun searchNotes (newText: String) {
            viewModelScope.launch {
                val listItem = searchNoteItemUseCase.searchNoteList(newText)
                _ldListNotes.value = listItem
            }

        }

    fun setZeroValueToList () {
        _ldListNotes.value = listOf<NoteItem>()
    }

}