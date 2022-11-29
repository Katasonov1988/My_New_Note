package com.example.mynewnote.domain

data class NoteItem(
    val id: Int,
    val header: String,
    val description: String,
    val date: String,
    val color: String
)
