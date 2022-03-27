package com.example.voicenotes.ui.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.voicenotes.App
import com.example.voicenotes.R
import com.example.voicenotes.ui.main.MainActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import javax.inject.Inject

class AuthFragment : Fragment() {

    //lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val authLauncher = VK.login(requireActivity()) { result: VKAuthenticationResult ->
            when (result) {
                is VKAuthenticationResult.Success -> {
                    Toast.makeText(requireActivity(), "URAAA", Toast.LENGTH_SHORT).show()
                }
                is VKAuthenticationResult.Failed -> {
                    Toast.makeText(requireActivity(), "(((((((((", Toast.LENGTH_SHORT).show()
                }
            }
        }
        authLauncher.launch(arrayListOf(VKScope.DOCS))

        return inflater.inflate(R.layout.fragment_auth, container, false)
    }
}