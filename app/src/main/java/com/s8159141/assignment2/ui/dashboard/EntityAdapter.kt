package com.s8159141.assignment2.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s8159141.assignment2.data.model.Entity
import com.s8159141.assignment2.databinding.ItemEntityBinding

/**
 * RecyclerView adapter for the dashboard list. Uses [ListAdapter] with DiffUtil for
 * efficient updates. Each row shows a summary (title + subtitle) without the description,
 * and the whole row is clickable to open the Details screen.
 */
class EntityAdapter(
    private val onClick: (Entity) -> Unit
) : ListAdapter<Entity, EntityAdapter.EntityViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val binding = ItemEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EntityViewHolder(
        private val binding: ItemEntityBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: Entity) {
            binding.tvTitle.text = entity.title
            binding.tvSubtitle.text = entity.subtitle
            binding.tvYear.text = entity.yearCompleted.toString()
            binding.root.setOnClickListener { onClick(entity) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Entity>() {
            override fun areItemsTheSame(oldItem: Entity, newItem: Entity) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Entity, newItem: Entity) =
                oldItem == newItem
        }
    }
}
