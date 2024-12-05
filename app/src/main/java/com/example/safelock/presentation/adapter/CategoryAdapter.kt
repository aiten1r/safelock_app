package com.example.safelock.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.safelock.databinding.CategoruItemBinding
import com.example.safelock.databinding.FragmentMainBinding
import com.example.safelock.domain.data.Category

class CategoryAdapter(
    private val onCategoryClick:(Category)->Unit
):ListAdapter<Category,CategoryAdapter.CategoryViewHolder>(DiffCallback()) {

    inner class CategoryViewHolder(private val binding: CategoruItemBinding):RecyclerView.ViewHolder(binding.root) {
       fun bind(category: Category){
           binding.ivCategoryicon.setImageResource(category.iconRes)
           binding.tvTitlecategory.text = category.title
           binding.tvCount.text = category.count.toString()

           itemView.setOnClickListener{onCategoryClick(category)}
       }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CategoruItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback:DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem == newItem

    }
}
