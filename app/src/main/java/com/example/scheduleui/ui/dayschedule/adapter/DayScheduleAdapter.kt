package com.example.scheduleui.ui.dayschedule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleui.R
import com.example.scheduleui.data.model.DaySchedule
import com.example.scheduleui.databinding.DayScheduleItemBinding
import com.example.scheduleui.util.format

class DayScheduleAdapter(
    private val context: Context,
    private val addSubject: (String) -> Unit,
    private val showDetailSubject: (Int) -> Unit
) : ListAdapter<DaySchedule, DayScheduleAdapter.DayScheduleViewHolder>(DiffCallback) {

    class DayScheduleViewHolder(private val binding: DayScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * This function is used to bind data for DayScheduleItem
         */
        fun bind(
            context: Context,
            daySchedule: DaySchedule,
            addSubject: (String) -> Unit,
            showDetailSubject: (Int) -> Unit
        ) {
            // Set date
            binding.day.text = daySchedule.date.format("ccc, dd/MM/yyyy")
            // Event add subject
            binding.day.setOnClickListener {
                addSubject(daySchedule.date.format(null))
            }

            // Init subjectAdapter
            val subjectAdapter = SubjectAdapter(showDetailSubject)
            // Set adapter for recycler view subject
            binding.SubjectList.adapter = subjectAdapter
            //Set layout manager for recycler view subject
            binding.SubjectList.layoutManager = LinearLayoutManager(context)

            // Submit list subject in this day
            if (daySchedule.subjects.isNullOrEmpty()) {
                binding.hasSubject.visibility = View.VISIBLE
                subjectAdapter.submitList(null)
            } else {
                binding.hasSubject.visibility = View.GONE
                subjectAdapter.submitList(daySchedule.subjects)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayScheduleViewHolder {
        return DayScheduleViewHolder(
            DayScheduleItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.day_schedule_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: DayScheduleViewHolder, position: Int) {
        holder.bind(
            context, getItem(position), addSubject, showDetailSubject
        )
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<DaySchedule>() {
            override fun areItemsTheSame(oldItem: DaySchedule, newItem: DaySchedule): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: DaySchedule, newItem: DaySchedule): Boolean {
                return oldItem.subjects?.joinToString() == newItem.subjects?.joinToString()
            }
        }
    }
}