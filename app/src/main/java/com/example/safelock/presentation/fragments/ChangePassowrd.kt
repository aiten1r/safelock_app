package com.example.safelock.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.safelock.R
import com.example.safelock.databinding.FragmentChangePassowrdBinding
import com.example.safelock.presentation.viewmodel.SharedViewModel
import kotlinx.coroutines.launch

class ChangePassowrd : Fragment() {

    private var _binding : FragmentChangePassowrdBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePassowrdBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edNewPasswrd.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val password = p0.toString().trim()
                if (password.length < 6 ){
                    binding.edNewPasswrd.error = "Пароль должен быть больше 6 символов"
                }else{
                    binding.edNewPasswrd.error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.btnChange.setOnClickListener {
            val oldPassword = binding.edOldPassword.text.toString().trim()
            val newPassword = binding.edNewPasswrd.text.toString().trim()

            if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val isSuccessful = sharedViewModel.changePassword(oldPassword, newPassword)
                if (!isSuccessful) {
                    Toast.makeText(requireContext(), "Старый пароль неверен", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                Toast.makeText(requireContext(), "Пароль успешно изменён", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }


    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}