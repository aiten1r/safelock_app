package com.example.safelock.presentation.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.safelock.R
import com.example.safelock.databinding.AlertDialogSendmassageBinding
import com.example.safelock.databinding.DialogWithChackboxBinding
import com.example.safelock.databinding.FragmentSettingsBinding
import com.example.safelock.presentation.viewmodel.SharedViewModel


class Settings : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSettingsPasswords.setOnClickListener{
            findNavController().navigate(R.id.action_settings_to_changePassowrd)
        }

        binding.tvNumberKayboard.setOnClickListener {
            val dialogBinding = DialogWithChackboxBinding.inflate(layoutInflater)

            val sharedPreferences =
                requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            val isNumericKeyboardEnabled = sharedPreferences.getBoolean("numericKeyboard", false)

            dialogBinding.cbNumbersKeyBoard.isChecked = isNumericKeyboardEnabled

            AlertDialog.Builder(requireContext())
                .setTitle("Настройка клавиатуры")
                .setMessage("Перед сохранением убедитесь что ваш пароль состоит только из цифр")
                .setView(dialogBinding.root)
                .setPositiveButton("Сохранить") { dialog, _ ->
                val isChacked = dialogBinding.cbNumbersKeyBoard.isChecked
                    sharedPreferences.edit().putBoolean("numericKeyboard",isChacked).apply()
                    dialog.dismiss()
                }
                .setNegativeButton("Отмена"){dialog,_->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        binding.tvChatDeveloper.setOnClickListener {
            val alertDialog = AlertDialogSendmassageBinding.inflate(layoutInflater)
            AlertDialog.Builder(requireContext())
                .setTitle("Написать разработчику")
                .setView(alertDialog.root)
                .setPositiveButton("Отправить") { dialog, _ ->
                    val message = alertDialog.edMessge.text.toString()
                    if (message.isNotEmpty()) {
                        sendMessageDeveloper(message)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Сообщение не может быть пустым",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        binding.tvDeletData.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Удалить все пароли")
                .setMessage("Вы уверены что хотите удалить все сахраненные пароли?")
                .setPositiveButton("Да") { _, _ ->
                    sharedViewModel.deletAllPasswords()
                    Toast.makeText(requireContext(), "Все пароли удалены", Toast.LENGTH_SHORT)
                        .show()
                }
                .setNegativeButton("Отмена", null)
                .create()
                .show()
        }

    }

    private fun sendMessageDeveloper(message: String) {
        val subject = "Сообщение от пользователя"
        val developerEmail = "12589aitek@gmail.com"

        val emaiIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(developerEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        try {
            startActivity(Intent.createChooser(emaiIntent, "Выберите почтовое приложение"))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Не удалось отправить сообщение", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}