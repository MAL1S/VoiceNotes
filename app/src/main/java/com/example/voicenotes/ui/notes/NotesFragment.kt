package com.example.voicenotes.ui.notes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.voicenotes.R
import com.example.voicenotes.databinding.FragmentNotesBinding
import com.example.voicenotes.domain.model.Note
import com.example.voicenotes.ui.player.PlayerService
import java.io.File
import java.io.IOException

class NotesFragment : Fragment(), OnNoteItemClickListener {

    private val binding by viewBinding(FragmentNotesBinding::bind)

    private lateinit var adapter: NotesFragmentRecyclerAdapter

    private val files = mutableListOf<File>()

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false

    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkForPermissions()

        setupVariables()
        setupUI()
    }

    private fun setupVariables() {
        adapter = NotesFragmentRecyclerAdapter(emptyList(), this)
    }

    private fun setupUI() {
        binding.apply {
            fabStartRecord.setOnClickListener {

                startRecording()


//                Toast.makeText(requireContext(), "Voice recording started", Toast.LENGTH_SHORT)
//                    .show()
            }

            fabStopRecord.setOnClickListener {
                stopRecording()
            }

            rcvNotes.adapter = adapter
        }
        updateUI()
    }

    private fun startRecording() {
        try {
            output =
                requireContext().filesDir.absolutePath + "/${count++}.m4a"
            mediaRecorder = MediaRecorder()

            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder?.setAudioEncodingBitRate(16*44100);
            mediaRecorder?.setAudioSamplingRate(44100);
            mediaRecorder?.setOutputFile(output)
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            updateUI()
            Toast.makeText(requireContext(), "Recording started!", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        if (state) {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            state = false
            updateUI()
        } else {
            Toast.makeText(requireContext(), "You are not recording right now!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun updateUI() {
        binding.apply {
            fabStartRecord.visibility = if (state) View.GONE else View.VISIBLE
            fabStopRecord.visibility = if (state) View.VISIBLE else View.GONE
        }

        adapter = NotesFragmentRecyclerAdapter(getAllNotes(), this)
        binding.rcvNotes.adapter = adapter
        adapter.notifyDataSetChanged()
        Log.d("AAA", adapter.getList().toString())
    }

    private fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()

        val dir =
            File(requireContext().filesDir.absolutePath.toString())

        for (file in dir.listFiles()) {

            notes.add(
                Note(
                    title = file.name,
                    path = file.path,
                    time = 0
                )
            )

        }

        return notes
    }

    private fun checkForPermissions() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.RECORD_AUDIO
//            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            val permissions = arrayOf(
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            )
//            ActivityCompat.requestPermissions(requireActivity(), permissions, 0)
//            return false
//        } else {
//            return true
//        }

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(requireContext(), "URA", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "NOT URA", Toast.LENGTH_SHORT).show()
                }
            }

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                // You can use the API that requires the permission.
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        }

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                // You can use the API that requires the permission.
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) -> {
                // You can use the API that requires the permission.
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.RECORD_AUDIO
                )
            }
        }
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(requireContext(), PlayerService::class.java)
        //intent.putExtra("path", Environment.getExternalStorageDirectory().absolutePath + "/recording.mp3")
        intent.putExtra("path", note.path)

        requireContext().startService(intent)
    }
}