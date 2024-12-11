package com.example.safelock.presentation.fragments.registration

import android.content.Intent
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
import com.example.safelock.data.sharedprefences.SharedPreferencesHelper
import com.example.safelock.databinding.FragmentCreateProfileBinding
import com.example.safelock.presentation.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateProfile : Fragment() {

    private var _binding: FragmentCreateProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cbIsSendToEmail.setOnCheckedChangeListener { _, isChacked ->
            binding.edEmail.visibility = if (isChacked) View.VISIBLE else View.GONE
        }

        val sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        binding.btnOk.setOnClickListener {
            val password = binding.edPassword.text.toString()
            val confirmPassword = binding.edReapetPassword.text.toString()
            val email = binding.edEmail.text.toString()
            val sendToEmail = binding.cbIsSendToEmail.isChecked

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Поля не могут быть пустыми", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (sendToEmail && email.isEmpty()) {
                Toast.makeText(requireContext(), "Введите email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            viewModel.registerUser(password, confirmPassword)

            if (sendToEmail) {
                sendPasswordToEmail(email, password)
            }

            observeUiState(sharedPreferencesHelper)
        }

    }

    private fun observeUiState(sharedPreferencesHelper: SharedPreferencesHelper) {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { message ->
                if (message.isNotEmpty()) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

                    // Если регистрация успешна, обновляем состояние первого запуска и переходим
                    if (message == "Профиль создан!") {
                        sharedPreferencesHelper.setFirstLaunch(false)
                        findNavController().navigate(R.id.loginFramgent)
                    }
                }
            }
        }
    }

    private fun sendPasswordToEmail(email: String, password: String) {

        val subject = "Ваш пароль"
        val message = "Ваш пароль: $password"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            startActivity(Intent.createChooser(intent, "Выберите почтовое приложение"))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Не удалось отправить email", Toast.LENGTH_SHORT)
                .show()
        }

    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}