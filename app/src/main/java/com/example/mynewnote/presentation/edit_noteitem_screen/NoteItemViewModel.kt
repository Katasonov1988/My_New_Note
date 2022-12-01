package com.example.mynewnote.presentation.edit_noteitem_screen

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.*
import com.example.mynewnote.data.repository.NoteListRepositoryImpl
import com.example.mynewnote.domain.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class NoteItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NoteListRepositoryImpl(application)

    private val getNoteItemUseCase = GetNoteItemUseCase(repository)
    private val addNoteItemUseCase = AddNoteItemUseCase(repository)
    private val editNoteItemUseCase = EditNoteItemUseCase(repository)
    private val deleteNoteItemUseCase = DeleteNoteItemUseCase(repository)

    private val _noteItem = MutableLiveData<NoteItem>()
    val noteItem: LiveData<NoteItem>
        get() = _noteItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getNoteItem(noteItemId: String) {
        viewModelScope.launch {
            val item = getNoteItemUseCase.getNoteItem(noteItemId)
            _noteItem.value = item
        }
    }

    fun deleteNoteFromNoteItemActivity(noteItem: NoteItem) {
        viewModelScope.launch {
            deleteNoteItemUseCase.deleteNoteItem(noteItem)
        }
    }

    fun addNoteItem(inputHeader: String?, inputDescription: String?) {
        val header = parseHeader(inputHeader)
        val description = parseDescription(inputDescription)
        val color = getItemColor()
        val date = getCurrentDate()
        val id = getId()
        val fieldsValid = validateInput(header, description)
        if (fieldsValid) {
            viewModelScope.launch {
                val noteItem = NoteItem(id, header, description, date, color)
                addNoteItemUseCase.addNoteItem(noteItem)
                finishWork()
            }

        }

    }

    fun editNoteItem(inputHeader: String?, inputDescription: String?) {
        val header = parseHeader(inputHeader)
        val description = parseDescription(inputDescription)
        val color = getItemColor()
        val date = getCurrentDate()
        val id = getId()
        val fieldsValid = validateInput(header, description)
        if (fieldsValid) {
            viewModelScope.launch {
                _noteItem.value?.let {
                    val item = it.copy(header = header, description = description, date = date)
                    editNoteItemUseCase.editNoteItem(item)
                    finishWork()
                }

            }

        }
    }

    fun deleteNoteItem(noteItem: NoteItem) {
        viewModelScope.launch {
            deleteNoteItemUseCase.deleteNoteItem(noteItem)
            finishWork()
        }
    }

    private fun parseHeader(inputHeader: String?): String {
        return inputHeader?.trim() ?: EMPTY_LINE
    }

    private fun parseDescription(inputDescription: String?): String {
        return inputDescription?.trim() ?: EMPTY_LINE
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat(PATTERN_FOR_TIME, Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getId(): String {
        return UUID.randomUUID().toString().replace(DASH, EMPTY_LINE).trim()
    }

    private fun getItemColor(): String {
        val firstColor = FIRST_COLOR
        val secondColor = SECOND_COLOR
        val thirdColor = THIRD_COLOR
        val fourthColor = FOURTH_COLOR
        val fifthColor = FIFTH_COLOR
        val listColor = arrayOf(firstColor, secondColor, thirdColor, fourthColor, fifthColor)
        val randomIndex = Random.nextInt(listColor.size)
        return listColor[randomIndex]
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    private fun validateInput(header: String, description: String): Boolean {
        var result = true
        if (header.isBlank() && description.isBlank()) {
            result = false
        }
        return result
    }


    companion object {
        private const val PATTERN_FOR_TIME = "dd.MM.yyyy HH:mm"
        private const val DASH = "-"
        private const val EMPTY_LINE = ""
        private const val FIRST_COLOR = "#FDCCCA"
        private const val SECOND_COLOR = "#C3D9FF"
        private const val THIRD_COLOR = "#BFE399"
        private const val FOURTH_COLOR = "#F4BD6A"
        private const val FIFTH_COLOR = "#E46B98"
    }

}