package com.example.scheduleui.ui.dayschedule.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleui.R
import com.example.scheduleui.databinding.FragmentDayScheduleListBinding
import com.example.scheduleui.findNavControllerSafely
import com.example.scheduleui.ui.dayschedule.adapter.DayScheduleAdapter
import com.example.scheduleui.ui.viewmodel.ScheduleViewModel
import com.example.scheduleui.ui.viewmodel.ScheduleViewModelFactory


class DayScheduleListFragment : Fragment() {

    private lateinit var binding: FragmentDayScheduleListBinding

    private val scheduleViewModel by activityViewModels<ScheduleViewModel> {
        ScheduleViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDayScheduleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        setupToolbar()

        // Khởi tạo danh sách day schedule
        loadRecyclerView()
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
        toolbar.inflateMenu(R.menu.day_schedule_fragment_menu)

        // Set MenuItem on click
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.comeBackMenuItem -> {
                    //todo xử lý sự kiện quay về ngày hiện tại
                    goToDay(dayPosition = 3)
                    Toast.makeText(context, "Comeback today", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.refreshMenuItem -> {
                    //todo xử lý làm mới
                    loadRecyclerView()
                    Toast.makeText(context, "Refresh", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.goToMenuItem -> {
                    //todo Xử lý sự kiện đi đến 1 ngày nào đó

                    val datePickerDialog = DatePickerDialog(
                        requireContext(),
                        { _, _, _, _ ->
                            goToDay(0)
                        },
                        2023,
                        9,
                        21
                    )

                    datePickerDialog.show()

                    Toast.makeText(context, "Go to ...", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.addRemindMenuItem -> {
                    //todo Xử lý sự kiện thêm nhắc nhở (thông báo)
                    Toast.makeText(context, "Add remind", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }

    private fun loadRecyclerView() {
        //todo đổ dữ liệu vào DayScheduleListFragment
        val adapter =
            DayScheduleAdapter(
                requireContext(),
                scheduleViewModel.subjects,
                addSubject = {
                    val action =
                        DayScheduleListFragmentDirections.actionDayScheduleListFragmentToAddDayScheduleFragment()
                    findNavControllerSafely()?.navigate(action)
                },
                showDetailSubject = { subjectId ->
                    val action =
                        DayScheduleListFragmentDirections.actionDayScheduleListFragmentToDetailDayScheduleFragment(
                            subjectId = subjectId
                        )
                    findNavControllerSafely()?.navigate(action)
                }
            )

        adapter.submitList(scheduleViewModel.daySchedules)
        binding.DayScheduleList.adapter = adapter
        binding.DayScheduleList.layoutManager = LinearLayoutManager(requireContext())

        goToDay(dayPosition = 3)
    }

    /**
     * This function is used to scroll a day schedule to head of screen
     *
     * @param dayPosition
     */
    private fun goToDay(dayPosition: Int) {
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