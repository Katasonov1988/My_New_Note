package com.example.mynewnote.presentation.note_list_adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.mynewnote.domain.NoteItem

class NoteItemDiffCallback: DiffUtil.ItemCallback<NoteItem>() {
    override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
        return oldItem == newItem
    }
}