package com.example.mynewnote.presentation.note_list_adapter

import android.graphics.Color
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
        if (!noteItem.header.isEmpty()) {
            holder.tvHeader.visibility = View.VISIBLE
            holder.tvHeader.text = noteItem.header
        } else {
            holder.tvHeader.visibility = View.GONE
        }
        if (noteItem.description.isEmpty()) {
            holder.tvDescription.visibility = View.GONE
        } else {
            holder.tvDescription.visibility = View.VISIBLE
            holder.tvDescription.text = noteItem.description
        }
        holder.tvData.text = noteItem.date.substring(0, noteItem.date.length - 5)
        val color = noteItem.color
        holder.cardView.setCardBackgroundColor(Color.parseColor(color))

        holder.view.setOnClickListener {
            onNoteItemClickListener?.invoke(noteItem)
        }
    }

    fun restoreItem(position: Int) {
        notifyItemChanged(position)
    }
}