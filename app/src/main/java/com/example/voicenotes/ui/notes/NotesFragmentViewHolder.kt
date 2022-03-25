package com.example.voicenotes.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.voicenotes.R
import com.example.voicenotes.databinding.ItemNoteBinding
import com.example.voicenotes.domain.model.Note

class NotesFragmentViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    private val onNoteItemClickListener: OnNoteItemClickListener
): RecyclerView.ViewHolder(inflater.inflate(R.layout.item_note, parent, false)) {

    private val binding by viewBinding(ItemNoteBinding::bind)

    fun bind(note: Note) {
        binding.apply {
            tvNoteTitle.text = note.title
        }

        itemView.setOnClickListener {
            onNoteItemClickListener.onNoteClick(note)
        }
    }
}