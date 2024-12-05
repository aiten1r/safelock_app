package com.example.safelock.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.safelock.R
import com.example.safelock.databinding.FragmentMainBinding
import com.example.safelock.domain.usecase.GetCategoriesUseCase
import com.example.safelock.presentation.adapter.CategoryAdapter
import com.example.safelock.presentation.viewmodel.MainViewModel
import com.example.safelock.presentation.viewmodelfactory.MainViewModelFactory

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? =
        null //присвоение _binding view и говорим что макет может быть null
    private val binding get() = _binding!! // получаем значение с _binding и говорим что он не равен null !!
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(GetCategoriesUseCase())
    }
    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       adapter = CategoryAdapter { category ->
           val bundle = Bundle().apply {
               putString("categoryTitle",category.title)
           }
            findNavController().navigate(R.id.action_mainFragment_to_detailsFragment,bundle)
        }
        binding.rvCategory.adapter = adapter

        viewModel.loadCategorys()
        observeViewModel()
    }

    private fun observeViewModel() {

        viewModel.categorys.observe(viewLifecycleOwner) { categories ->
            adapter.submitList(categories)
        }

    }

    override fun onDestroyView() {
        _binding = null // обнуляем _binding что бы он не ссылался на view каторый уже уничтожен
        super.onDestroyView()
    }

}