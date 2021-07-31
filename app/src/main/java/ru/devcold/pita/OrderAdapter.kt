package ru.devcold.pita

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.devcold.pita.databinding.ItemOrderBinding

class OrderAdapter : ListAdapter<Order, OrderAdapter.ViewHolder>(DiffUtilCallback) {

    companion object {
        object DiffUtilCallback : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = currentList[position]
        val context = holder.itemView.context
        holder.title.text = currentItem.title

        Glide
            .with(context)
            .load(currentItem.photos[currentItem.previewPhotoIndex])
            .into(holder.image)
    }

    class ViewHolder(binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.title
        val image: ImageView = binding.image
    }
}