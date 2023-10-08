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
import com.example.scheduleui.data.DaySchedule
import com.example.scheduleui.databinding.DayScheduleItemBinding
import com.example.scheduleui.util.formatDayScheduleDate

class DayScheduleAdapter(
    private val context: Context,
    private val addSubject: (String) -> Unit,
    private val submitListSubjects: (SubjectAdapter, Int, View) -> Unit,
    private val showDetailSubject: (Int) -> Unit
) :
    ListAdapter<DaySchedule, DayScheduleAdapter.DayScheduleViewHolder>(
        DiffCallback
    ) {

    class DayScheduleViewHolder(private val binding: DayScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * This function is used to bind data for DayScheduleItem
         */
        fun bind(
            context: Context,
            daySchedule: DaySchedule,
            addSubject: (String) -> Unit,
            submitListSubjects: (SubjectAdapter, Int, View) -> Unit,
            showDetailSubject: (Int) -> Unit,
        ) {
            // Set date
            binding.day.text = daySchedule.day.formatDayScheduleDate()
            // Event add subject
            binding.day.setOnClickListener {
                addSubject(daySchedule.day.formatDayScheduleDate())
            }

            // Init subjectAdapter
            val subjectAdapter = SubjectAdapter(showDetailSubject)
            // Submit list subject in this day
            submitListSubjects(subjectAdapter, daySchedule.id, binding.hasSubject)
            // Set adapter for recycler view subject
            binding.SubjectList.adapter = subjectAdapter
            //Set layout manager for recycler view subject
            binding.SubjectList.layoutManager = LinearLayoutManager(context)
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
            context,
            getItem(position),
            addSubject,
            submitListSubjects,
            showDetailSubject
        )
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<DaySchedule>() {
            override fun areItemsTheSame(oldItem: DaySchedule, newItem: DaySchedule): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DaySchedule, newItem: DaySchedule): Boolean {
                return oldItem.day == newItem.day
            }
        }
    }
}