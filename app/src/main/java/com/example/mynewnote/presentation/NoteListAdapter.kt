package com.example.mynewnote.presentation

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.mynewnote.R
import com.example.mynewnote.domain.NoteItem

class NoteListAdapter : ListAdapter<NoteItem, NoteItemViewHolder>(NoteItemDiffCallback()) {

    var onNoteItemClickListener: ((NoteItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.note_item,
            parent,
            false
        )
        return NoteItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        val noteItem = getItem(position)
        if (noteItem.header.isEmpty()) {
            holder.tvHeader.setVisibility(View.GONE)
        } else {
            holder.tvHeader.text = noteItem.header
        }
        if (noteItem.description.isEmpty()) {
            holder.tvDescription.setVisibility(View.GONE)
        } else {
            holder.tvDescription.text = noteItem.description
        }
        holder.tvData.text = noteItem.date.substring(0, noteItem.date.length - 5)
        val color = noteItem.color
        Log.i("color", color)
        holder.cardView.setCardBackgroundColor(Color.parseColor(color))

        holder.view.setOnClickListener {
            onNoteItemClickListener?.invoke(noteItem)
        }
    }
}