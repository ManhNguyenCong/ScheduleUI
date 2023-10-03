package com.example.scheduleui.ui.dayschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.scheduleui.R
import com.example.scheduleui.data.ScheduleApplication
import com.example.scheduleui.data.Subject
import com.example.scheduleui.databinding.FragmentDetailDayScheduleBinding
import com.example.scheduleui.ui.dayschedule.viewmodel.DayScheduleViewModel
import com.example.scheduleui.ui.dayschedule.viewmodel.DayScheduleViewModelFactory
import com.example.scheduleui.util.findNavControllerSafely
import com.example.scheduleui.util.formatDayScheduleDate
import com.example.scheduleui.util.formatDayScheduleTime

class DetailDayScheduleFragment : Fragment() {

    // Arguments of DetailDatScheduleFragment in nav_graph
    private val args: DetailDayScheduleFragmentArgs by navArgs()

    // Binding to fragment_detail_day_schedule
    private lateinit var binding: FragmentDetailDayScheduleBinding

    // DayScheduleViewModel
    private val viewModel: DayScheduleViewModel by activityViewModels {
        DayScheduleViewModelFactory(
            (activity?.application as ScheduleApplication).database.scheduleDao()
        )
    }

    // Subject
    private lateinit var subject: Subject

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailDayScheduleBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        setupToolbar()
        // Get and load subject information
        viewModel.getSubjectById(args.subjectId).observe(this.viewLifecycleOwner) {
            subject = it
            loadSubjectInfor(subject)
        }
    }

    /**
     * This function is used to set up menu, menuItemSelected for DetailDayScheduleFragment
     */
    private fun setupToolbar() {
        // Get toolbar from activity
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar) ?: return

        // Clean old menu
        toolbar.menu.clear()
        // Inflate new menu layout
        toolbar.inflateMenu(R.menu.detail_fragment_menu)
        // Set onclick for menu item
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.deleteObjectMenuItem -> {
                    // Delete subject
                    deleteSubject(subject)
                    // Navigate to dayScheduleListFragment
                    val action =
                        DetailDayScheduleFragmentDirections.actionDetailDayScheduleFragmentToDayScheduleListFragment()
                    findNavControllerSafely()?.navigate(action)

                    true
                }

                R.id.editObjectMenuItem -> {
                    // Navigate to AddDayScheduleFragment
                    val action =
                        DetailDayScheduleFragmentDirections.actionDetailDayScheduleFragmentToAddDayScheduleFragment(
                            subjectId = args.subjectId
                        )
                    findNavControllerSafely()?.navigate(action)
                    true
                }

                else -> false
            }
        }
    }

    /**
     * This function is used to load subject information
     *
     */
    private fun loadSubjectInfor(subject: Subject) {
        // Set name subject
        binding.name.text = subject.name
        // Set day, time start and time end
        viewModel.daySchedules.observe(this.viewLifecycleOwner) { daySchedules ->
            binding.time.text = String.format(
                "%s - %s\n%s",
                subject.timeStart.formatDayScheduleTime(),
                subject.timeEnd.formatDayScheduleTime(),
                daySchedules.find { it.id == subject.dayScheduleId }?.day?.formatDayScheduleDate()
            )
        }
        // Set location if it isn't empty
        if (subject.location.isEmpty()) {
            binding.location.visibility = View.GONE
        } else {
            binding.location.text = subject.location
        }
        // Set teacher if it isn't empty
        if (subject.teacher.isEmpty()) {
            binding.teacher.visibility = View.GONE
        } else {
            binding.teacher.text = subject.teacher
        }
        // Set notes if it isn't empty
        if (subject.notes.isEmpty()) {
            binding.notes.visibility = View.GONE
        } else {
            binding.notes.text = subject.notes
        }
    }

    /**
     * This function is used to delete a subject
     *
     * @param subject
     */
    private fun deleteSubject(subject: Subject) {
        viewModel.deleteSubject(subject)
    }
}