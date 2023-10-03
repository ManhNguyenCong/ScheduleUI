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

class NotificationAdapter(private val showPopupMenu: (View, Int) -> Unit): ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(
    DiffCallback
) {
    class NotificationViewHolder(private val binding: NotificationItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            notification: Notification,
            showPopupMenu: (View, Int) -> Unit) {
            binding.name.text = notification.name
            binding.popupMenu.setOnClickListener {
                showPopupMenu(binding.popupMenu, notification.id)
            }

//            binding.time.text = notification.time
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