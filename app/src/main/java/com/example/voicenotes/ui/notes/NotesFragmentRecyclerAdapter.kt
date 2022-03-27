package com.example.voicenotes.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voicenotes.domain.model.Note

class NotesFragmentRecyclerAdapter(
    private val notesList: List<Note>,
    private val onNoteItemClickListener: OnNoteItemClickListener
): RecyclerView.Adapter<NotesFragmentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesFragmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NotesFragmentViewHolder(inflater, parent, onNoteItemClickListener)
    }

    override fun onBindViewHolder(holder: NotesFragmentViewHolder, position: Int) {
        val note = notesList[position]
        holder.bind(note = note, position = position)
    }

    override fun getItemCount() = notesList.size

    fun getList() = notesList
}