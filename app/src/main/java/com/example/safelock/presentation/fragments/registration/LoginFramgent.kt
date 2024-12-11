package com.example.safelock.presentation.fragments.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.safelock.R
import com.example.safelock.databinding.FragmentLoginFramgentBinding
import com.example.safelock.presentation.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFramgent : Fragment() {

    private var _binding: FragmentLoginFramgentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginFramgentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        binding.btnLogin.setOnClickListener {
            val inputPassword = binding.edPassword.text.toString()
            // Передача пароля в ViewModel
            viewModel.login(inputPassword)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { message ->
                if (message.isNotEmpty()) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

                    // Если вход успешный, можно перейти на главный экран
                    if (message == "Успешный вход!") {
                        binding.ivClosedIcon.visibility = View.GONE
                        findNavController().navigate(R.id.mainFragment)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}