package com.example.safelock.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.safelock.R
import com.example.safelock.databinding.FragmentMainBinding
import com.example.safelock.presentation.adapter.CategoryAdapter
import com.example.safelock.presentation.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? =
        null //присвоение _binding view и говорим что макет может быть null
    private val binding get() = _binding!! // получаем значение с _binding и говорим что он не равен null !!
    private lateinit var adapter: CategoryAdapter
    val sharedViewModel: SharedViewModel by activityViewModels()

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
                putString("categoryTitle", category.title)
                putInt("categoryId", category.id)
            }
            findNavController().navigate(R.id.action_mainFragment_to_detailsFragment, bundle)
        }
        binding.rvCategory.adapter = adapter

        sharedViewModel.clearPassword()
        sharedViewModel.loadCategorys()
        observeViewModel()

        sharedViewModel.categories.observe(viewLifecycleOwner) { updateCategories ->
            (binding.rvCategory.adapter as CategoryAdapter).submitList(updateCategories)
        }

    }

    private fun observeViewModel() {

        //observe - подписка на Livedata падписка значит если что то измениться в Livedata то сообщи мне
        //viewLifecycleOwner это обьект каторый управляет жизненым циклом view здесь он говорит что он будет подписываться на LiveData до тех пор пока он будет сущетсвовать чтобы не было утечек в помяти
        sharedViewModel.categories.observe(viewLifecycleOwner) { categories ->
            //submitList это метод от ListAdapter каторый менеят тот обьект или добовляет или удаляет если список изменился
            adapter.submitList(categories)
        }

    }

    override fun onDestroyView() {
        _binding = null // обнуляем _binding что бы он не ссылался на view каторый уже уничтожен
        super.onDestroyView()
    }

}