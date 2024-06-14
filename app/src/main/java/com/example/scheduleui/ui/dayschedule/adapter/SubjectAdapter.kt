package com.example.scheduleui.ui.dayschedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleui.data.model.Subject
import com.example.scheduleui.databinding.SubjectItemBinding
import com.example.scheduleui.util.format

class SubjectAdapter(private val detailSubject: (Int) -> Unit) :
    ListAdapter<Subject, SubjectAdapter.SubjectViewHolder>(DiffCallback) {
    class SubjectViewHolder(private val binding: SubjectItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * This function is used to bind data for SubjectItem
         */
        fun bind(subject: Subject) {
            binding.name.text = subject.name
            binding.timeStart.text = subject.timeStart.format("HH:mm")
            binding.location.text = subject.location
            binding.teacher.text = subject.teacher
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder(
            SubjectItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            detailSubject(current.id)
        }
        holder.bind(current)
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Subject>() {
            override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.date == newItem.date &&
                        oldItem.timeStart == newItem.timeStart &&
                        oldItem.timeEnd == newItem.timeEnd &&
                        oldItem.location == newItem.location &&
                        oldItem.teacher == newItem.teacher &&
                        oldItem.notes == newItem.notes
            }
        }
    }
}