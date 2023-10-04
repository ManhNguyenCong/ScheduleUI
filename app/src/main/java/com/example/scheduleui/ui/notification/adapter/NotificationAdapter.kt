package com.example.scheduleui.ui.notification.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleui.R
import com.example.scheduleui.data.Notification
import com.example.scheduleui.databinding.NotificationItemBinding
import com.example.scheduleui.util.formatDayScheduleDate
import com.example.scheduleui.util.formatDayScheduleTime

class NotificationAdapter(private val showPopupMenu: (View, Notification) -> Unit): ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(
    DiffCallback
) {
    class NotificationViewHolder(private val binding: NotificationItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            notification: Notification,
            showPopupMenu: (View, Notification) -> Unit) {
            // Set up name
            binding.name.text = notification.name
            // Set up event open popup menu
            binding.popupMenu.setOnClickListener {
                showPopupMenu(binding.popupMenu, notification)
            }
            // Set up time
            if(notification.loopOption) { // Loop every day
                binding.time.text = String.format("%s - %s",
                    notification.time.formatDayScheduleTime(),
                    "Hằng ngày")
            } else { // Don't loop
                binding.time.text = String.format("%s, %s",
                    notification.time.formatDayScheduleDate(),
                    notification.time.formatDayScheduleTime())
            }
            // Set up content
            binding.notes.text = notification.notes
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            NotificationItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(
            getItem(position),
            showPopupMenu)
    }

    companion object {
        val DiffCallback = object: DiffUtil.ItemCallback<Notification>() {
            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}