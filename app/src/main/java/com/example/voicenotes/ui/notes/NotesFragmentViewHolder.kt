package com.example.voicenotes.ui.notes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.voicenotes.R
import com.example.voicenotes.databinding.ItemNoteBinding
import com.example.voicenotes.domain.model.Note
import com.example.voicenotes.utils.convertTimeToString

class NotesFragmentViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    private val onNoteItemClickListener: OnNoteItemClickListener
) : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_note, parent, false)) {

    private val binding by viewBinding(ItemNoteBinding::bind)

    fun bind(note: Note, position: Int) {
        val duration = note.overallDuration
        binding.apply {
            imgNoteStart.visibility = if (note.isPlaying) View.INVISIBLE else View.VISIBLE
            imgNotePause.visibility = if (note.isPlaying) View.VISIBLE else View.INVISIBLE
        }

        if (!note.isPlaying) {
            if (note.currentDuration != null && note.currentDuration != 0 && note.overallDuration != null) {
                bindTitleAndProgress(note)

            } else {
                binding.apply {
                    tvNoteTitle.text = note.title
                    progressNotePlaying.progress = 0

                    if (duration != null) {
                        tvNoteDuration.text = convertTimeToString(duration/1000)
                    }
                }
            }
        } else {
            bindTitleAndProgress(note)
        }
        itemView.setOnClickListener {
            onNoteItemClickListener.onNoteClick(note = note, position = position)
        }
    }

    private fun bindTitleAndProgress(note: Note) {
        val duration = note.overallDuration
        val currentDuration = note.currentDuration

        binding.apply {
            tvNoteTitle.text = note.title

            if (duration != null && currentDuration != null) {
                Log.d("FFF", "current = $currentDuration overall = $duration")
                progressNotePlaying.progress = ((1.0*currentDuration / duration) * 100).toInt()
                Log.d("FFF", "${progressNotePlaying.progress}")

                tvNoteDuration.text =
                    convertTimeToString(currentDuration/1000) + " / " + convertTimeToString(duration/1000)
            }
        }
    }
}