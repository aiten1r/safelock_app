package com.example.safelock.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val passwordTitle = arguments?.getString("passwordTitle")
        val categoryId = arguments?.getInt("categoryId") ?: 0
        (activity as AppCompatActivity).supportActionBar?.title = passwordTitle

        binding.btnNegative.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnPositiv.setOnClickListener {
            val title = binding.edTitle.text.toString()
            val password = binding.edPassword.text.toString()
            val description = binding.edDescription.text.toString()

            val newPassword = Password(0, title, password, description, categoryId)
            sharedViewModel.addPassword(newPassword)

            sharedViewModel.updateCategoryCount(categoryId)
            findNavController().navigateUp()
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}