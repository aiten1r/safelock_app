package com.example.safelock.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.PrimaryKey
import com.example.safelock.databinding.DateilsItemBinding
import com.example.safelock.domain.data.Password

class PasswordAdapter(
    private val onItemLongClick: (Password) -> Unit,
    private val onItemClick: (Password) -> Unit
) :
    ListAdapter<Password, PasswordAdapter.PasswordViewHolder>(PasswordDiffUtilCallbeck()) {
    class PasswordViewHolder(private val binding: DateilsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            password: Password,
            onItemLongClick: (Password) -> Unit,
            onItemClick: (Password) -> Unit
        ) {
            binding.tvItemName.text = password.title

            binding.root.setOnLongClickListener {
                onItemLongClick(password)
                true
            }
            binding.root.setOnClickListener {
                onItemClick(password)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val binding = DateilsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PasswordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        holder.bind(getItem(position), onItemLongClick, onItemClick)
    }
}

private class PasswordDiffUtilCallbeck : DiffUtil.ItemCallback<Password>() {
    override fun areItemsTheSame(oldItem: Password, newItem: Password): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Password, newItem: Password): Boolean {
        return oldItem == newItem
    }

}
