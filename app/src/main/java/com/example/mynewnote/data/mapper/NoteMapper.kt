package com.example.mynewnote.data.mapper

import com.example.mynewnote.data.database.NoteDBModel
import com.example.mynewnote.domain.NoteItem

class NoteMapper {
    fun mapDBModelToEntity(dbModel: NoteDBModel) = NoteItem(
            id = dbModel.id,
            header = dbModel.header,
            description = dbModel.description,
            date = dbModel.date,
            color = dbModel.color
        )


    fun mapEntityToDBModel (noteItem: NoteItem) = NoteDBModel (
        id = noteItem.id,
        header = noteItem.header,
        description = noteItem.description,
        date = noteItem.date,
        color = noteItem.color
            )
    }