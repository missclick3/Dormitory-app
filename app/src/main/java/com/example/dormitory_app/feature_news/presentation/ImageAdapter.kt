package com.example.dormitory_app.feature_news.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dormitory_app.R
import com.example.dormitory_app.feature_news.external.messages.FileDto

class ImageAdapter : ListAdapter<FileDto, ImageAdapter.ViewHolder>(DiffCallback()) {
    class DiffCallback : DiffUtil.ItemCallback<FileDto>() {
        override fun areItemsTheSame(oldItem: FileDto, newItem: FileDto): Boolean {
            return oldItem.fileId == newItem.fileId
        }

        override fun areContentsTheSame(oldItem: FileDto, newItem: FileDto): Boolean {
            return oldItem.fileId == newItem.fileId && oldItem.url == newItem.url && oldItem.fileName == newItem.fileName
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView = itemView.findViewById<ImageView>(R.id.imageView)

        fun bindData(item: FileDto) {
            Glide.with(itemView)
                .load(item.url)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.image_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = getItem(position)
        holder.bindData(image)
    }
}