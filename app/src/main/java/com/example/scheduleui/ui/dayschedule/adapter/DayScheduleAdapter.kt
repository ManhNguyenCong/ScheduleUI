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
import com.example.scheduleui.databinding.DayScheduleItemBinding
import com.example.scheduleui.data.DaySchedule
import com.example.scheduleui.data.Subject

class DayScheduleAdapter(
    private val context: Context,
    private val subjects: List<Subject>,
    private val addSubject: () -> Unit,
    private val showDetailSubject: (Int) -> Unit
) :
    ListAdapter<DaySchedule, DayScheduleAdapter.DayScheduleViewHolder>(
        DiffCallback
    ) {
    class DayScheduleViewHolder(private val binding: DayScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //Gắn dữ liệu vào giao diện
        fun bind(
            context: Context,
            daySchedule: DaySchedule,
            subjects: List<Subject>,
            addSubject: () -> Unit,
            showDetailSubject: (Int) -> Unit
        ) {

            binding.day.text = daySchedule.day
            binding.day.setOnClickListener {
                addSubject()
            }

            //todo add subjectAdapter
            val subjectInDay =
                subjects.filter { subject -> subject.dayScheduleId == daySchedule.id }

            if (subjectInDay.isEmpty()) {
                binding.hasSubject.visibility = View.VISIBLE
            } else {
                binding.hasSubject.visibility = View.GONE
                val adapter = SubjectAdapter(showDetailSubject)

                adapter.submitList(subjectInDay)
                binding.SubjectList.adapter = adapter
                binding.SubjectList.layoutManager = LinearLayoutManager(context)
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
            context,
            getItem(position),
            subjects,
            addSubject,
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