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
import com.example.scheduleui.ScheduleApplication
import com.example.scheduleui.data.localdatabase.SettingPreferences
import com.example.scheduleui.data.model.Subject
import com.example.scheduleui.data.repository.NotifRepository
import com.example.scheduleui.data.repository.SubjectRepository
import com.example.scheduleui.databinding.FragmentDetailDayScheduleBinding
import com.example.scheduleui.ui.DayScheduleViewModel
import com.example.scheduleui.ui.DayScheduleViewModelFactory
import com.example.scheduleui.util.findNavControllerSafely
import com.example.scheduleui.util.format

class DetailDayScheduleFragment : Fragment() {

    // Arguments of DetailDatScheduleFragment in nav_graph
    private val args: DetailDayScheduleFragmentArgs by navArgs()

    // Binding to fragment_detail_day_schedule
    private lateinit var binding: FragmentDetailDayScheduleBinding

    // DayScheduleViewModel
    private val viewModel: DayScheduleViewModel by activityViewModels {
        DayScheduleViewModelFactory(
            SubjectRepository((activity?.application as ScheduleApplication).database.scheduleDao()),
            NotifRepository((activity?.application as ScheduleApplication).database.scheduleDao()),
            SettingPreferences(requireContext())
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
            bind(subject)
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
                    viewModel.delete(subject)
                    findNavControllerSafely()?.navigateUp()
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
    private fun bind(subject: Subject) {
        // Set name subject
        binding.name.text = subject.name
        // Set day, time start and time end
        binding.time.text = String.format(
            "%s - %s\n%s",
            subject.timeStart.format("HH:mm"),
            subject.timeEnd.format("HH:mm"),
            subject.date.format("dd/MM/yyyy")
        )
        binding.location.text = subject.location
        binding.teacher.text = subject.teacher
        binding.notes.text = subject.notes
    }
}