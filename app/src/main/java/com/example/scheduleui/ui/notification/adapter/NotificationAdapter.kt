package com.example.scheduleui.ui.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleui.R
import com.example.scheduleui.data.model.Notification
import com.example.scheduleui.databinding.NotificationItemBinding
import com.example.scheduleui.util.format
import java.time.LocalDateTime

class NotificationAdapter(
    private val context: Context,
    private val showPopupMenu: (View, Notification) -> Unit
) : ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(DiffCallback) {
    class NotificationViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, notification: Notification, showPopupMenu: (View, Notification) -> Unit) {
            // Set up name
            binding.name.text = notification.name
            // Set up event open popup menu
            binding.popupMenu.setOnClickListener {
                showPopupMenu(binding.popupMenu, notification)
            }
            if (notification.isLoop) { // Loop every day
                binding.time.text = String.format("%s - %s",
                    notification.time.toLocalTime().format("HH:mm"),
                    "Hằng ngày")
            } else { // Don't loop
                binding.time.text = String.format("%s, %s",
                    notification.time.toLocalDate().format(pattern = "ccc, dd/MM/yyyy"),
                    notification.time.toLocalTime().format(pattern = "HH:mm"))
            }
            // Set up content
            binding.notes.text = notification.notes

            if (notification.time.isBefore(LocalDateTime.now())) {
                binding.name.setTextColor(context.resources.getColor(R.color.gray, null))
                binding.notes.setTextColor(context.resources.getColor(R.color.gray, null))
                binding.time.setTextColor(context.resources.getColor(R.color.gray, null))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            NotificationItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.notification_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(
            context,
            getItem(position), showPopupMenu
        )
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Notification>() {
            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}