package com.example.scheduleui.ui.dayschedule.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleui.R
import com.example.scheduleui.data.ScheduleApplication
import com.example.scheduleui.databinding.FragmentDayScheduleListBinding
import com.example.scheduleui.ui.dayschedule.adapter.DayScheduleAdapter
import com.example.scheduleui.ui.dayschedule.viewmodel.DayScheduleViewModel
import com.example.scheduleui.ui.dayschedule.viewmodel.DayScheduleViewModelFactory
import com.example.scheduleui.util.findNavControllerSafely
import com.example.scheduleui.util.setDefaultTime
import java.util.Calendar


class DayScheduleListFragment : Fragment() {

    private lateinit var binding: FragmentDayScheduleListBinding

    // DayScheduleAdapter for RecyclerView
    private var dayScheduleAdapter: DayScheduleAdapter? = null

    // Is load fragment completed?
    private var isLoadFragmentCompleted = false

    // Is wait time to autoAddDay?
    private var isWaitTime = true

    // Today position for event comeback today
    private var todayPosition: Int = -1
        set(value) {
            // When first time change value (open app)
            if (field == -1) {
                goToDay(value)
            }
            field = value
        }

    // View model
    private val dayScheduleViewModel: DayScheduleViewModel by activityViewModels {
        DayScheduleViewModelFactory(
            (activity?.application as ScheduleApplication).database.scheduleDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDayScheduleListBinding.inflate(inflater, container, false)

        // Load elements of fragment
        loadFragment()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        setupToolbar()
        // Add day schedule for first time use
        dayScheduleViewModel.addDaySchedulesForFirstTime()
        // Load daySchedule list
        loadListDaySchedule()
        // Scroll to today
        goToDay(todayPosition)
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
                    // Scroll to today
                    goToDay(todayPosition)
                    true
                }

                R.id.refreshMenuItem -> {
                    // Re-load fragment
                    loadFragment()
                    // Re-load list day schedule
                    loadListDaySchedule()

                    true
                }

                R.id.goToMenuItem -> {
                    // Create date picker dialog
                    val datePickerDialog = DatePickerDialog(
                        requireContext(),
                        { _, year, month, dayOfMonth ->
                            // Get day choose
                            val dayChoose =
                                Calendar.Builder().setDate(year, month, dayOfMonth).build()
                            // Get current list
                            val currentList =
                                dayScheduleAdapter?.currentList ?: return@DatePickerDialog
                            if (dayScheduleViewModel.checkAutoAddDayCompleted(
                                    currentList,
                                    dayChoose
                                )
                            ) {
                                // If day choose in dayScheduleList go to...
                                goToDay(currentList.indexOf(currentList.find { daySchedule ->
                                    daySchedule.day == dayChoose
                                }))
                            } else {
                                // If day choose not in dayScheduleList, auto add
                                if (!isWaitTime) {
                                    // Add day to day choose
                                    dayScheduleViewModel.addDayScheduleToDay(dayChoose)
                                    // Set wait time
                                    isWaitTime = true
                                }
                            }

                            checkAutoAddCompleted(dayChoose)
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH) + 1,
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    )
                    // Show date picker dialog
                    datePickerDialog.show()
                    true
                }

                R.id.addRemindMenuItem -> {
                    // Navigate to add notification
                    val action = DayScheduleListFragmentDirections.actionDayScheduleListFragmentToAddNotificationFragment()
                    findNavControllerSafely()?.navigate(action)
                    true
                }

                else -> false
            }
        }
    }

    /**
     * This function is used to load elements of fragment
     */
    private fun loadFragment() {
        dayScheduleAdapter =
            DayScheduleAdapter(
                requireContext(),
                addSubject = { date ->
                    val action =
                        DayScheduleListFragmentDirections.actionDayScheduleListFragmentToAddDayScheduleFragment(
                            date = date
                        )
                    findNavControllerSafely()?.navigate(action)
                },
                submitListSubjects = { subjectAdapter, dayScheduleId, hasSubjectView ->
                    dayScheduleViewModel.getSubjectByDayScheduleId(dayScheduleId)
                        .observe(this.viewLifecycleOwner) { subjects ->
                            subjectAdapter.submitList(subjects)
                            if(subjects.isEmpty()) {
                                hasSubjectView.visibility = View.VISIBLE
                            } else {
                                hasSubjectView.visibility = View.GONE
                            }
                        }
                },
                showDetailSubject = { subjectId ->
                    val action =
                        DayScheduleListFragmentDirections.actionDayScheduleListFragmentToDetailDayScheduleFragment(
                            subjectId = subjectId
                        )
                    findNavControllerSafely()?.navigate(action)
                }
            )

        binding.DayScheduleList.adapter = dayScheduleAdapter
        binding.DayScheduleList.layoutManager = LinearLayoutManager(requireContext())
    }

    /**
     * This function is used to load data of list day schedule
     */
    private fun loadListDaySchedule() {
        dayScheduleViewModel.daySchedules.observe(this.viewLifecycleOwner) { daySchedules ->
            daySchedules?.also {
                dayScheduleAdapter?.submitList(daySchedules)
            }

            // Get today position to event comeback today
            todayPosition = daySchedules.indexOf(
                daySchedules.find { it.day == Calendar.getInstance().setDefaultTime() }
            )
        }

        // Set auto add
        autoAddDaySchedule()
    }

    /**
     * This function is used to add day schedule automatically
     * when user scrolls to first item or last item in list
     *
     */
    private fun autoAddDaySchedule() {
        binding.DayScheduleList.setOnScrollChangeListener { _, _, _, _, _ ->
            // Target day auto add
            var targetDay: Calendar? = null

            // Process case: when open app, auto add day schedule
            if (todayPosition != -1 && !isLoadFragmentCompleted &&
                (binding.DayScheduleList.layoutManager as LinearLayoutManager)
                    .findFirstCompletelyVisibleItemPosition() == todayPosition
            ) {
                isLoadFragmentCompleted = true
                isWaitTime = false
            }

            // Don't auto add when isWaitTime
            if (isWaitTime) {
                return@setOnScrollChangeListener
            }

            // Get dayScheduleAdapter
            val adapter = dayScheduleAdapter ?: return@setOnScrollChangeListener
            // Get position of first visible Item now
            val firstVisibleItemPosition =
                (binding.DayScheduleList.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            // Get position of last visible Item now
            val lastVisibleItemPosition =
                (binding.DayScheduleList.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

            // If firstVisibleItem is firstItem in list and isn't wait time,
            // add 7 day before first day in list
            if (firstVisibleItemPosition == 0 && !isWaitTime) {
                // Set isWaitTime when start auto add
                isWaitTime = true
                // Get first day in list
                val firstDay = adapter.currentList[0].day
                // Get target day
                targetDay = Calendar.Builder()
                    .setInstant(firstDay.timeInMillis - 7 * DayScheduleViewModel.DAY_TO_MILLIS)
                    .build()
                // Add day to first day - 7
                dayScheduleViewModel.addDayScheduleToDay(targetDay!!)
            }

            // If lastVisibleItem is lastItem in list and isn't wait time,
            // add 7 day after last day in list
            if (lastVisibleItemPosition == adapter.currentList.size - 1 && !isWaitTime) {
                // Set isWaitTime when start auto add
                isWaitTime = true
                // Get last day in list
                val lastDay = adapter.currentList[adapter.currentList.size - 1].day
                // Get targetDay
                targetDay = Calendar.Builder()
                    .setInstant(lastDay.timeInMillis + 7 * DayScheduleViewModel.DAY_TO_MILLIS)
                    .build()
                // Add day to last day + 7
                dayScheduleViewModel.addDayScheduleToDay(targetDay!!)
            }

            // Check auto add day completed?
            checkAutoAddCompleted(targetDay ?: return@setOnScrollChangeListener)
        }
    }

    /**
     * This function is used to check "auto add day schedule is completed?"
     */
    private fun checkAutoAddCompleted(targetDay: Calendar) {
        dayScheduleViewModel.daySchedules.observe(this.viewLifecycleOwner) { daySchedules ->
            if (isWaitTime) {
                // Check auto add completed?
                isWaitTime = !dayScheduleViewModel.checkAutoAddDayCompleted(
                    daySchedules,
                    targetDay
                )
            }
        }
    }

    /**
     * This function is used to scroll a day schedule to head of screen
     *
     * @param dayPosition
     */
    private fun goToDay(dayPosition: Int) {
        if (dayPosition == -1) return

        val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
            // Thiết lập vị trí mà child xuất hiện (start -> trên cùng)
            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }
        }

        // Gán vị trí muốn scroll đến
        smoothScroller.targetPosition = dayPosition
        binding.DayScheduleList.layoutManager?.startSmoothScroll(smoothScroller)
    }
}