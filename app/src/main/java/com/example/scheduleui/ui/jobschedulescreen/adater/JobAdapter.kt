package com.example.scheduleui.ui.jobschedulescreen.adater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleui.R
import com.example.scheduleui.data.JobSchedule
import com.example.scheduleui.databinding.JobItemBinding

class JobAdapter(
    private val showDetailJob: (Int) -> Unit
) : ListAdapter<JobSchedule, JobAdapter.JobViewHolder>(DiffCallback) {

    class JobViewHolder(private val binding: JobItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(jobSchedule: JobSchedule) {
            binding.name.text = jobSchedule.name
//            binding.time.text = jobSchedule.time
            binding.location.text = jobSchedule.location
            binding.notes.text = jobSchedule.notes
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        return JobViewHolder(
            JobItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.job_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            showDetailJob(current.id)
        }
        holder.bind(current)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<JobSchedule>() {
            override fun areItemsTheSame(oldItem: JobSchedule, newItem: JobSchedule): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: JobSchedule, newItem: JobSchedule): Boolean {
                return oldItem.time == newItem.time
            }
        }
    }
}