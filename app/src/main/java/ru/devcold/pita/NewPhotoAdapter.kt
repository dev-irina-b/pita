package ru.devcold.pita

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.devcold.pita.databinding.ItemOrderBinding

class NewPhotoAdapter : ListAdapter<Uri, NewPhotoAdapter.ViewHolder>(DiffUtilCallback) {

    companion object {
        object DiffUtilCallback : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri) = oldItem.path == newItem.path

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = currentList[position]
    }

    class ViewHolder(binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.image
    }
}