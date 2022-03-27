package com.example.voicenotes.ui.notes

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.voicenotes.App
import com.example.voicenotes.R
import com.example.voicenotes.databinding.FragmentNotesBinding
import com.example.voicenotes.domain.model.Note
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import java.io.File
import javax.inject.Inject


class NotesFragment : Fragment(), OnNoteItemClickListener {

    companion object {
        var player: MediaPlayer? = null
        var isPlaying = false
        var isFirstPlaying = true
        val playerLiveData = MutableLiveData<Int>()
        var currentPosition = 0
    }

    private val binding by viewBinding(FragmentNotesBinding::bind)

    @Inject
    lateinit var notesFragmentViewModel: NotesFragmentViewModel

    private lateinit var adapter: NotesFragmentRecyclerAdapter

    private val notes = mutableListOf<Note>()
    private var currentIndex = -1

    private var authLauncher: ActivityResultLauncher<Collection<VKScope>>? = null

    private var playerIntent: Intent? = null
    private val handler = Handler(Looper.getMainLooper())
    private val playingNote = object : Runnable {
        override fun run() {
            Log.d("FFF", "handler started $currentIndex $player")
            if (currentIndex != -1) {
                if (player != null) {
                    val position =
                        ((player!!.currentPosition % (1000 * 60 * 60)) % (1000 * 60))

//                    if (notes[currentIndex].isPlaying && notes[currentIndex].currentDuration == position) {
//                        Log.d("QQQ", "${notes[currentIndex]} == $position")
//                        //updateNoteToNotPlaying(currentIndex)
//                        return
//                    }
                    if (position >= notes[currentIndex].overallDuration!! ||
                        notes[currentIndex].isPlaying &&
                        notes[currentIndex].currentDuration == position &&
                        position + 350 >= notes[currentIndex].overallDuration!!) {
                        updateNoteToNotPlaying(currentIndex)
                        return
                    }

                    notes[currentIndex].apply {
                        isPlaying = true
                        currentDuration = position
                        Log.d("DDD", "$currentDuration")
                    }
                    currentPosition = position
                    adapter.notifyItemChanged(currentIndex)
                    handler.postDelayed(this, 100)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        if (authLauncher == null) {
            authLauncher = VK.login(requireActivity()) { result: VKAuthenticationResult ->
                when (result) {
                    is VKAuthenticationResult.Success -> {
                        Toast.makeText(requireActivity(), "URAAA", Toast.LENGTH_SHORT).show()
                    }
                    is VKAuthenticationResult.Failed -> {
                        Toast.makeText(requireActivity(), "(((((((((", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkForPermissions()

        setupVariables()
        fillNotesList()
        setupUI(view)
    }

    override fun onResume() {
        super.onResume()

        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notes, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_auth -> {
//                view?.findNavController()?.navigate(R.id.action_notesFragment_to_authFragment)

                authLauncher?.launch(arrayListOf(VKScope.DOCS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupVariables() {
        adapter = NotesFragmentRecyclerAdapter(emptyList(), this)
    }

    private fun setupUI(view: View) {
        binding.apply {
            rcvNotes.adapter = adapter
            (rcvNotes.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            btnGetToRecordingScreen.setOnClickListener {
//                stopPlaying()
                if (isPlaying) stopPlayer(currentIndex)
                view.findNavController().navigate(R.id.action_notesFragment_to_recordFragment)
            }
        }
        playerLiveData.observe(viewLifecycleOwner) {
            if (it == 1 && isPlaying) {
                handler.postDelayed(playingNote, 0)
            }
        }
        updateUI()
    }

    private fun updateUI() {
        adapter = NotesFragmentRecyclerAdapter(notes, this)
        binding.rcvNotes.adapter = adapter
        adapter.notifyDataSetChanged()
    }
//
//    private fun startPlaying() {
//        isPlaying = true
////        player?.start()
//        player?.seekTo(currentPosition)
//        handler.postDelayed(playingNote, 0)
//    }

//    private fun stopPlaying() {
//        isPlaying = false
//        //updateNoteToNotPlaying(currentIndex)
//        //player?.stop()
//        handler.removeCallbacks(playingNote)
//    }

    private fun fillNotesList() {
        notes.clear()
        val dir =
            File(requireContext().filesDir.absolutePath.toString())

        for (file in dir.listFiles()) {
            Log.d("AAA", "$file")

            var durationInMilliseconds: Int? = null

            try {
                val uri = Uri.parse(file.absolutePath)
                val mmr = MediaMetadataRetriever()
                mmr.setDataSource(requireActivity().applicationContext, uri)
                val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                durationInMilliseconds = duration?.toInt()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            notes.add(
                Note(
                    title = file.name,
                    path = file.path,
                    overallDuration = durationInMilliseconds,
                    currentDuration = 0,
                    isPlaying = false
                )
            )
        }
    }

    private fun checkForPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(requireActivity(), permissions, 0)
        }

//        val requestPermissionLauncher =
//            registerForActivityResult(
//                ActivityResultContracts.RequestPermission()
//            ) { isGranted: Boolean ->
//                if (isGranted) {
//                    Toast.makeText(requireContext(), "URA", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(requireContext(), "NOT URA", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        when (PackageManager.PERMISSION_GRANTED) {
//            ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) -> {
//                // You can use the API that requires the permission.
//            }
//            else -> {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                requestPermissionLauncher.launch(
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//            }
//        }
//
//        when (PackageManager.PERMISSION_GRANTED) {
//            ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) -> {
//                // You can use the API that requires the permission.
//            }
//            else -> {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                requestPermissionLauncher.launch(
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//            }
//        }
//
//        when (PackageManager.PERMISSION_GRANTED) {
//            ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.RECORD_AUDIO
//            ) -> {
//                // You can use the API that requires the permission.
//            }
//            else -> {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                requestPermissionLauncher.launch(
//                    Manifest.permission.RECORD_AUDIO
//                )
//            }
//        }
    }

    private fun startPlayer(note: Note) {
//        playerIntent = Intent(requireContext(), PlayerService::class.java)
//        playerIntent?.putExtra("path", note.path)
//        requireContext().startService(playerIntent)
        notesFragmentViewModel.startPlayer(note.path)
        handler.postDelayed(playingNote, 500)
    }

    private fun stopPlayer(position: Int) {
        //if (player != null) requireContext().stopService(playerIntent)
        updateNoteToPause(position)
        handler.removeCallbacks(playingNote)
        notesFragmentViewModel.stopPlayer()
    }

    private fun updateNoteToPause(position: Int) {
        notes[position].apply {
            isPlaying = false
            currentDuration = ((currentPosition % (1000 * 60 * 60)) % (1000 * 60))
        }
        isPlaying = false
        adapter.notifyItemChanged(position)
    }

    private fun updateNoteToNotPlaying(position: Int) {
        notes[position].apply {
            isPlaying = false
            currentDuration = 0
        }
        isPlaying = false
        currentPosition = 0
        adapter.notifyItemChanged(position)
    }

    override fun onNoteClick(note: Note, position: Int) {
        isFirstPlaying = false
        if (currentIndex != -1 && currentIndex != position) {
            Log.d("FFF", "SWAP")
            stopPlayer(currentIndex)
            updateNoteToNotPlaying(currentIndex)
            currentIndex = position
            currentPosition = 0
            startPlayer(note)
            return
        }
        currentIndex = position
        if (isPlaying) {
            currentPosition = player!!.currentPosition
            Log.d("FFF", "STOP")
            stopPlayer(position)
        } else {
            Log.d("FFF", "START")
            startPlayer(note)
        }
    }
}