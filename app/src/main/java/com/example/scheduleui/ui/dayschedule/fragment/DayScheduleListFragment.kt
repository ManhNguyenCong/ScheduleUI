package com.example.scheduleui.ui.dayschedule.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import com.example.scheduleui.R
import com.example.scheduleui.ScheduleApplication
import com.example.scheduleui.data.localdatabase.SettingPreferences
import com.example.scheduleui.data.model.DaySchedule
import com.example.scheduleui.data.repository.NotifRepository
import com.example.scheduleui.data.repository.SubjectRepository
import com.example.scheduleui.databinding.FragmentDayScheduleListBinding
import com.example.scheduleui.ui.DayScheduleViewModel
import com.example.scheduleui.ui.DayScheduleViewModelFactory
import com.example.scheduleui.ui.dayschedule.adapter.DayScheduleAdapter
import com.example.scheduleui.util.findNavControllerSafely
import java.time.LocalDate


class DayScheduleListFragment : Fragment() {

    private lateinit var binding: FragmentDayScheduleListBinding

    // DayScheduleAdapter for RecyclerView
    private var dayScheduleAdapter: DayScheduleAdapter? = null

    // View model
    private val dayScheduleViewModel: DayScheduleViewModel by activityViewModels {
        DayScheduleViewModelFactory(
            SubjectRepository((activity?.application as ScheduleApplication).database.scheduleDao()),
            NotifRepository((activity?.application as ScheduleApplication).database.scheduleDao()),
            SettingPreferences(requireContext())
        )
    }

    private var date: LocalDate = LocalDate.now()
    private lateinit var ldSubjects: LiveData<List<DaySchedule>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDayScheduleListBinding.inflate(inflater, container, false)

        // Setup toolbar
        setupToolbar()

        dayScheduleAdapter = DayScheduleAdapter(requireContext(), addSubject = {
            val action =
                DayScheduleListFragmentDirections.actionDayScheduleListFragmentToAddDayScheduleFragment(
                    date = it
                )
            findNavControllerSafely()?.navigate(action)
        }, showDetailSubject = { subjectId ->
            val action =
                DayScheduleListFragmentDirections.actionDayScheduleListFragmentToDetailDayScheduleFragment(
                    subjectId = subjectId
                )
            findNavControllerSafely()?.navigate(action)
        })
        binding.rvDaySchedules.adapter = dayScheduleAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ldSubjects = dayScheduleViewModel.getSubjectsInRecentDays(date)
        ldSubjects.observe(this.viewLifecycleOwner) {
            dayScheduleAdapter?.submitList(it)
        }

        binding.btnBack.setOnClickListener {
            date = date.plusDays(-7)
            loadSchedule(date)
        }

        binding.btnNext.setOnClickListener {
            date = date.plusDays(7)
            loadSchedule(date)
        }
    }

    private fun loadSchedule(date: LocalDate) {
        ldSubjects.removeObservers(this.viewLifecycleOwner)
        ldSubjects = dayScheduleViewModel.getSubjectsInRecentDays(date)
        ldSubjects.observe(this.viewLifecycleOwner) {
            dayScheduleAdapter?.submitList(null)
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
                    date = LocalDate.now()
                    loadSchedule(date)
                    true
                }

                R.id.refreshMenuItem -> {
                    loadSchedule(date)
                    true
                }

                R.id.goToMenuItem -> {
                    val datePickerDialog = DatePickerDialog(
                        requireContext(),
                        { datePicker, _, _, _ ->
                            date = LocalDate.of(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth)
                            loadSchedule(date)
                        },
                        date.year,
                        date.monthValue - 1,
                        date.dayOfMonth
                    )
                    datePickerDialog.create()
                    datePickerDialog.show()
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