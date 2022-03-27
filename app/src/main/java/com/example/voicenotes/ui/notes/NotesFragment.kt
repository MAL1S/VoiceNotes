package com.example.voicenotes.ui.notes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.voicenotes.R
import com.example.voicenotes.databinding.FragmentNotesBinding
import com.example.voicenotes.domain.model.Note
import com.example.voicenotes.ui.services.PlayerService
import java.io.File

class NotesFragment : Fragment(), OnNoteItemClickListener {

    private val binding by viewBinding(FragmentNotesBinding::bind)

    private lateinit var adapter: NotesFragmentRecyclerAdapter

    private val files = mutableListOf<File>()

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
        setupUI(view)
    }

    override fun onResume() {
        super.onResume()

        updateUI()
    }

    private fun setupVariables() {
        adapter = NotesFragmentRecyclerAdapter(emptyList(), this)
    }

    private fun setupUI(view: View) {
        binding.apply {
            rcvNotes.adapter = adapter
            btnGetToRecordingScreen.setOnClickListener {
                view.findNavController().navigate(R.id.action_notesFragment_to_recordFragment)
            }
        }
        updateUI()
    }

    private fun updateUI() {
        adapter = NotesFragmentRecyclerAdapter(getAllNotes(), this)
        binding.rcvNotes.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()

        val dir =
            File(requireContext().filesDir.absolutePath.toString())

        for (file in dir.listFiles()) {
            Log.d("AAA", "$file")

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