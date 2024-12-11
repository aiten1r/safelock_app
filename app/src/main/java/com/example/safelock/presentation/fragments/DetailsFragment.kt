package com.example.safelock.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.safelock.R
import com.example.safelock.databinding.FragmentDetailsBinding
import com.example.safelock.domain.data.Password
import com.example.safelock.presentation.adapter.PasswordAdapter
import com.example.safelock.presentation.viewmodel.SharedViewModel
import com.example.safelock.presentation.viewmodel.SortType
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInAnimator

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryId = arguments?.getInt("categoryId") ?: return
        val categoryTitle = arguments?.getString("categoryTitle")

        sharedViewModel.setCategoryId(categoryId)

        val adapter = PasswordAdapter(onItemLongClick = { password ->
            // Удаление элемента по долгому нажатию
            showDeleteConfirmationDialog(password)
        }, onItemClick = { password ->
            val bundle = Bundle().apply {
                putInt("idKey", password.id)
                putString("passwordTitle", categoryTitle)
                putString("titleKey", password.title)
                putString("passwordKey", password.password)
                putString("descriptionKey", password.description)
                putInt("categoryId", categoryId)
            }
            findNavController().navigate(R.id.action_detailsFragment_to_addPassword, bundle)
        })

        binding.rvPassword.adapter = adapter
        binding.rvPassword.itemAnimator = FadeInAnimator()

        sharedViewModel.passwords.observe(viewLifecycleOwner) { passwords ->
            adapter.submitList(passwords)
        }

        (activity as AppCompatActivity).supportActionBar?.title =
            categoryTitle // мы сообщаем что activity это AppCompatActivity чтобы получить его метод supportActionBar каторый доступен только в AppCompatActivity

        binding.fabAddpassword.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("categoryId", categoryId)
                putString("passwordTitle", categoryTitle)
            }
            findNavController().navigate(R.id.action_detailsFragment_to_addPassword, bundle)
        }

    }



    private fun showDeleteConfirmationDialog(password: Password) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удалить пароль?")
            .setMessage("Вы уверены, что хотите удалить этот элемент?")
            .setPositiveButton("Удалить") { _, _ ->
                sharedViewModel.deletePassword(password)
            }
            .setNegativeButton("Отмена", null)
            .create()
            .show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}