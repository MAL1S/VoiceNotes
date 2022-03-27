package com.example.voicenotes.ui.notes

import com.example.voicenotes.domain.model.Note

interface OnNoteItemClickListener {

    fun onNoteClick(note: Note, position: Int)
}