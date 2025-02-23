package com.example.safelock.presentation.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.safelock.R
import com.example.safelock.databinding.FragmentAddPasswordBinding
import com.example.safelock.domain.data.Password
import com.example.safelock.presentation.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPassword : Fragment() {

    private var _binding: FragmentAddPasswordBinding? = null
    private val binding get() = _binding!!
    val sharedViewModel: SharedViewModel by activityViewModels()

    private var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemId = arguments?.getInt("idKey")
        val passwordTitle = arguments?.getString("passwordTitle")
        val categoryId = arguments?.getInt("categoryId") ?: 0
        val itemTitle = arguments?.getString("titleKey")
        val itemPassword = arguments?.getString("passwordKey")
        val itemDesrciption = arguments?.getString("descriptionKey")
        (activity as AppCompatActivity).supportActionBar?.title = passwordTitle
        binding.edTitle.setText(itemTitle)
        binding.edPassword.setText(itemPassword)
        binding.edDescription.setText(itemDesrciption)

        binding.btnNegative.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnPositiv.setOnClickListener {
            val title = binding.edTitle.text.toString()
            val password = binding.edPassword.text.toString()
            val description = binding.edDescription.text.toString()

            if (title.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Название не может быть пустым",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (itemId != null && itemId != 0) {
                val updatedPassword = Password(
                    id = itemId,
                    title = title,
                    password = password,
                    description = description,
                    categoryId = categoryId
                )
                sharedViewModel.updatePassword(updatedPassword)
            } else {
                val newPassword = Password(
                    id = 0,
                    title = title,
                    password = password,
                    description = description,
                    categoryId = categoryId
                )
                sharedViewModel.addPassword(newPassword, categoryId)
            }
            findNavController().navigateUp()
        }

        binding.ivCopytitle.setOnClickListener {
            val textCopy = binding.edTitle.text.toString()
            if (textCopy.isNotEmpty()) {
                val clipBoard =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", textCopy)
                clipBoard.setPrimaryClip(clip)
            }
        }

        binding.ivCopypassword.setOnClickListener {
            val passwordTextCopy = binding.edPassword.text.toString()
            if (passwordTextCopy.isNotEmpty()) {
                val clipBoard =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", passwordTextCopy)
                clipBoard.setPrimaryClip(clip)
            }
        }
        binding.ivRandomPossword.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Сгененировать пароль")
                .setPositiveButton("Да") { _, _ ->
                    val randomPassword = generateRandomPassword()
                    binding.edPassword.setText(randomPassword)
                }
                .setNegativeButton("Нет", null)
                .create()
                .show()
        }
        sharedViewModel.togglePasswordVisibility.observe(viewLifecycleOwner) {
            isPasswordVisible = !isPasswordVisible
            updatePasswordVisibility()
        }

    }

    private fun updatePasswordVisibility() {
        if (isPasswordVisible) {
            binding.edPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            binding.edPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        binding.edPassword.setSelection(binding.edPassword.text.length)
    }

    private fun generateRandomPassword(length: Int = 12): String {
        val chars =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
