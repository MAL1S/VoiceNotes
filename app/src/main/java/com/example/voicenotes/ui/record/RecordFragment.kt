package com.example.voicenotes.ui.record

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.work.*
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.voicenotes.App
import com.example.voicenotes.R
import com.example.voicenotes.databinding.FragmentRecordBinding
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.concurrent.timerTask

class RecordFragment : Fragment() {

    private val binding by viewBinding(FragmentRecordBinding::bind)

    @Inject
    lateinit var recordViewModel: RecordFragmentViewModel

    private lateinit var recorder: Recorder
    private var wasFileNameSet = false

    private val handler = Handler(Looper.getMainLooper())
    private var seconds = 0
    private val timerTask = object: Runnable {
        override fun run() {
            binding.tvRecordingDuration.text = "Идет запись ${seconds / 60}:${if (seconds%60<10) 0 else ""}${seconds % 60}"
            seconds++
            handler.postDelayed(this, 1000)
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
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupVariables()
        setupUI(view)
    }

    private fun setupVariables() {
        recorder = Recorder(requireContext())
    }

    private fun setupUI(view: View) {
        binding.apply {
            fabStartRecord.setOnClickListener {
                val fileName = if (etFileName.text.toString().isNotEmpty()) {
                    wasFileNameSet = true
                    etFileName.text.toString()
                } else {
                    ""
                }
                recorder.startRecording(fileName = fileName)
                updateButtons(true)
                startTimer()
            }

            fabStopRecord.setOnClickListener {
                recorder.stopRecording()
                cancelTimer()
                updateButtons(false)
                //view.findNavController().popBackStack()
            }

            btnSaveFile.setOnClickListener {
                var fileName: String? = null
                if (!wasFileNameSet) {
                    if (etFileName.text.toString().isNotEmpty()) {
                        fileName = etFileName.text.toString()
                    } else {
                        recordViewModel.getFileName()
                    }
                    Toast.makeText(requireContext(), "$fileName", Toast.LENGTH_SHORT).show()
                }
                recorder.saveFile(fileName = fileName)
                recorder.releasePlayer()
                view.findNavController().popBackStack()
            }

            btnDeleteFile.setOnClickListener {
                recorder.deleteFile()
                recorder.releasePlayer()
                view.findNavController().popBackStack()
            }
        }
    }

    private fun startTimer() {
        showRecordingDuration()
    }

    private fun cancelTimer() {
        handler.removeCallbacks(timerTask)
        binding.tvRecordingDuration.text = "Запись закончена ${seconds / 60}:${if (seconds%60<10) 0 else ""}${seconds % 60}"
    }

    private fun updateButtons(flag: Boolean) {
        binding.apply {
            fabStartRecord.visibility = if (flag) View.GONE else View.VISIBLE
            fabStopRecord.visibility = if (flag) View.VISIBLE else View.GONE
            btnSaveFile.visibility = if (flag) View.GONE else View.VISIBLE
            btnDeleteFile.visibility = if (flag) View.GONE else View.VISIBLE
        }
    }

    private fun showRecordingDuration() {
        handler.postDelayed(timerTask, 1000)
    }
}


