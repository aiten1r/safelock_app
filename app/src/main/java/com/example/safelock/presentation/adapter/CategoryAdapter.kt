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
    //это лямда ыункция каторый примнимает Category и нечего не возвращает
    private val onCategoryClick: (Category) -> Unit //Unit указывает что onCategoryClick нечего не возвращает
    //ListAdapter это клас работающий с ResyclerView каторый умеет менять только тот елемент каторый изменился
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(DiffCallback()) {

    //inner используеться для обьявление внутренного класса,Каторый имеет доступ к полям и методом внешнего класса в данном случае это класс CategoryAdapter
    //CategoryViewHolder связывает елемнты Category с интерфейсом -(TextView,ImageView и т.д)
    inner class CategoryViewHolder(private val binding: CategoruItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //binding.root это ссылка на главный контейнер макета
        //bind метод каторый связывает данные
        fun bind(category: Category) {
            binding.ivCategoryicon.setImageResource(category.iconRes)
            binding.tvTitlecategory.text = category.title
            binding.tvCount.text = category.count.toString()

            //item View это базавый механизм для доступа карновой View
            binding.root.setOnClickListener { onCategoryClick(category) }
            //itemView.setOnClickListener { onCategoryClick(category) }
        }

    }

    //метод каторый вызываеться,когда нужео создать новый ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            CategoruItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    //метод вызываеться когда нужно заполнить ViewHolder
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    //клас каторый помогает ListAdapter понять какие елементы списка изменились
    class DiffCallback : DiffUtil.ItemCallback<Category>() {

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem.id == newItem.id //сравнивает два елемнтв по id и если id совпадает то это считаеться одним и тем же елементом

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem == newItem //сравнивае все садержиме обьектов Category Если садержимое совпадает то обнавление не требуеться

    }
}
