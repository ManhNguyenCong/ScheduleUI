package com.example.scheduleui.ui.jobschedulescreen.adater

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
import com.example.scheduleui.data.JobSchedule
import com.example.scheduleui.databinding.JobScheduleItemBinding

class JobScheduleAdapter(
    private val context: Context,
    private val jobSchedules: List<JobSchedule>,
    private val showPopupMenu: (View, Int) -> Unit,
    private val showDetailJob: (Int) -> Unit
) :
    ListAdapter<DaySchedule, JobScheduleAdapter.JobScheduleViewHolder>(DiffCallback) {

    class JobScheduleViewHolder(private val binding: JobScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            jobSchedules: List<JobSchedule>,
            daySchedule: DaySchedule,
            showPopupMenu: (View, Int) -> Unit,
            showDetailJob: (Int) -> Unit
        ) {

//            binding.Day.text = daySchedule.day
            binding.popupMenu.setOnClickListener {
                showPopupMenu(binding.popupMenu, daySchedule.id)
            }

            binding.hasJob.visibility = View.VISIBLE

            val jobScheduleInDay =
                jobSchedules.filter { jobSchedule -> jobSchedule.dayScheduleId == daySchedule.id }

            if (jobScheduleInDay.isNotEmpty()) {
                binding.hasJob.visibility = View.GONE
                val adapter = JobAdapter(showDetailJob)
                adapter.submitList(jobScheduleInDay)
                binding.SubjectList.adapter = adapter
                binding.SubjectList.layoutManager = LinearLayoutManager(context)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobScheduleViewHolder {
        return JobScheduleViewHolder(
            JobScheduleItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.job_schedule_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: JobScheduleViewHolder, position: Int) {
        holder.bind(
            context,
            jobSchedules,
            getItem(position),
            showPopupMenu,
            showDetailJob
        )
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DaySchedule>() {
            override fun areItemsTheSame(oldItem: DaySchedule, newItem: DaySchedule): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DaySchedule, newItem: DaySchedule): Boolean {
                return oldItem.day == newItem.day
            }


        }
    }
}