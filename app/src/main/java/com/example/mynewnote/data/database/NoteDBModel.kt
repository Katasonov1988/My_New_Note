package com.example.mynewnote.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteDBModel(
    @PrimaryKey
    val id: String,
    val header: String,
    val description: String,
    val date: String,
    val color: String
)