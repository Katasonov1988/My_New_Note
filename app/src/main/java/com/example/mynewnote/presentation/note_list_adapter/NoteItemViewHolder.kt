package com.example.mynewnote.presentation.note_list_adapter

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewnote.R

class NoteItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val tvHeader = view.findViewById<TextView>(R.id.tvHeader)
    val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
    val tvData = view.findViewById<TextView>(R.id.tvDate)
    val cardView = view.findViewById<CardView>(R.id.cardViewNote)
}