package com.example.scheduleui.ui.dayschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.scheduleui.R
import com.example.scheduleui.ScheduleApplication
import com.example.scheduleui.data.repository.SubjectRepository
import com.example.scheduleui.databinding.FragmentDayScheduleListBinding
import com.example.scheduleui.ui.dayschedule.adapter.DayScheduleAdapter
import com.example.scheduleui.ui.dayschedule.viewmodel.DayScheduleViewModel
import com.example.scheduleui.ui.dayschedule.viewmodel.DayScheduleViewModelFactory
import com.example.scheduleui.util.findNavControllerSafely


class DayScheduleListFragment : Fragment() {

    private lateinit var binding: FragmentDayScheduleListBinding

    // DayScheduleAdapter for RecyclerView
    private var dayScheduleAdapter: DayScheduleAdapter? = null

    // View model
    private val dayScheduleViewModel: DayScheduleViewModel by activityViewModels {
        DayScheduleViewModelFactory(
            SubjectRepository(
                (activity?.application as ScheduleApplication).database.scheduleDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDayScheduleListBinding.inflate(inflater, container, false)

        // Setup toolbar
        setupToolbar()

        dayScheduleAdapter = DayScheduleAdapter(
            requireContext(),
            addSubject = {

            },
            showDetailSubject = {

            }
        )
        binding.rvDaySchedules.adapter = dayScheduleAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dayScheduleViewModel.getSubjectsInRecentDays().observe(this.viewLifecycleOwner) {
            dayScheduleAdapter?.submitList(it)
        }
    }

    /**
     * This function is used to setup menu, menuItemSelected for DayScheduleListFragment
     *
     */
    private fun setupToolbar() {
        // Get toolbar form activity
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar) ?: return

        // Clean old menu
        toolbar.menu?.clear()
        // Inflate menu layout
        toolbar.inflateMenu(R.menu.list_fragment_menu)
        // Set MenuItem on click
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.comeBackMenuItem -> {
                    // TODO Scroll to today
                    true
                }

                R.id.refreshMenuItem -> {
                    // TODO Refresh
                    true
                }

                R.id.goToMenuItem -> {
                    // TODO Go to a day
                    true
                }

                R.id.addRemindMenuItem -> {
                    // Navigate to add notification
                    findNavControllerSafely()?.navigate(R.id.notificationFragment)
                    true
                }

                else -> false
            }
        }
    }
}