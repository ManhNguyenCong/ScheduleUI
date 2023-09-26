package com.example.scheduleui.ui.jobschedulescreen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleui.R
import com.example.scheduleui.databinding.FragmentJobScheduleListBinding
import com.example.scheduleui.findNavControllerSafely
import com.example.scheduleui.ui.jobschedulescreen.adater.JobScheduleAdapter
import com.example.scheduleui.ui.viewmodel.ScheduleViewModel
import com.example.scheduleui.ui.viewmodel.ScheduleViewModelFactory

class JobScheduleListFragment : Fragment() {

    private lateinit var binding: FragmentJobScheduleListBinding

    private val viewModel: ScheduleViewModel by activityViewModels {
        ScheduleViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJobScheduleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        setupToolbar()

        // Load list of JobSchedule
        loadRecycleView()
    }

    /**
     * This function is used to setup menu, menuItemSelected for JobScheduleLisFragment
     */
    private fun setupToolbar() {
        // Get toolbar for activity
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar) ?: return

        // Clean old menu
        toolbar.menu.clear()
        // Inflate new menu layout
        toolbar.inflateMenu(R.menu.day_schedule_fragment_menu)
        // Repair menu item
        toolbar.menu.findItem(R.id.addRemindMenuItem).title =
            getString(R.string.manageJobMenuItemTitle)
        // Set onClick for menu item
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.comeBackMenuItem -> {
                    Toast.makeText(context, "Comeback today", Toast.LENGTH_SHORT).show()
                    //todo xử lý sự kiện quay về ngày hiện tại
                    goToDay(dayPosition = 3)
                    true
                }

                R.id.refreshMenuItem -> {
                    Toast.makeText(context, "Refresh", Toast.LENGTH_SHORT).show()
                    //todo xử lý sự kiện làm mới fragment
                    loadRecycleView()
                    true
                }

                R.id.goToMenuItem -> {
                    Toast.makeText(context, "Go to ...", Toast.LENGTH_SHORT).show()
                    //todo scroll đến 1 ngày nhất định
                    goToDay(0)
                    true
                }

                R.id.addRemindMenuItem -> {
                    Toast.makeText(context, "Quản lý sự kiện", Toast.LENGTH_SHORT).show()
                    // Add new jobSchedule
                    val action =
                        JobScheduleListFragmentDirections.actionJobScheduleListFragmentToAddJobScheduleFragment()
                    findNavControllerSafely()?.navigate(action)
                    true
                }

                else -> false
            }
        }
    }

    /**
     * This function is used to load job schedule list
     */
    private fun loadRecycleView() {
        val adapter = JobScheduleAdapter(
            requireContext(),
            viewModel.jobs,
            showPopupMenu = { view, dayScheduleId ->
                val popupMenu = PopupMenu(context, view)

                popupMenu.menu.add(getString(R.string.addJobMenuItemTitle))
                popupMenu.menu.add(getString(R.string.addRequestMenuItemTitle))
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.title) {
                        getString(R.string.addJobMenuItemTitle) -> {
                            // Navigate to AddJobScheduleFragment
                            val action =
                                JobScheduleListFragmentDirections.actionJobScheduleListFragmentToAddJobScheduleFragment(
                                    dayScheduleId
                                )
                            findNavControllerSafely()?.navigate(action)
                            true
                        }

                        getString(R.string.addRequestMenuItemTitle) -> {
                            //todo navigate to add request
                            Toast.makeText(context, "Add request", Toast.LENGTH_SHORT).show()
                            true
                        }

                        else -> false
                    }
                }

                popupMenu.show()

            },
            showDetailJob = { jobScheduleId ->
                // Navigate to detailJobScheduleFragment
                val action =
                    JobScheduleListFragmentDirections.actionJobScheduleListFragmentToDetailJobScheduleFragment(
                        jobScheduleId = jobScheduleId
                    )
                findNavControllerSafely()?.navigate(action)
            }
        )

        adapter.submitList(viewModel.daySchedules)
        binding.jobScheduleList.adapter = adapter
        binding.jobScheduleList.layoutManager = LinearLayoutManager(requireContext())

        // Scroll đếm ngày hiện tại
        goToDay(dayPosition = 3)
    }

    /**
     * This function is used to scroll a job schedule to head of screen
     *
     * @param dayPosition
     */
    private fun goToDay(dayPosition: Int) {
        val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
            // Thiết lập vị trí của child là trên cùng
            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }
        }

        // Gán vị trí child muốn scroll đến
        smoothScroller.targetPosition = dayPosition
        binding.jobScheduleList.layoutManager?.startSmoothScroll(smoothScroller)
    }
}