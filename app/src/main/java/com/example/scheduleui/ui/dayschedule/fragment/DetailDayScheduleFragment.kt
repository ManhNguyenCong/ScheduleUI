package com.example.scheduleui.ui.dayschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.scheduleui.R
import com.example.scheduleui.data.Subject
import com.example.scheduleui.databinding.FragmentDetailDayScheduleBinding
import com.example.scheduleui.findNavControllerSafely
import com.example.scheduleui.ui.viewmodel.ScheduleViewModel
import com.example.scheduleui.ui.viewmodel.ScheduleViewModelFactory

class DetailDayScheduleFragment : Fragment() {

    private val args: DetailDayScheduleFragmentArgs by navArgs()

    private lateinit var binding: FragmentDetailDayScheduleBinding

    private val viewModel: ScheduleViewModel by activityViewModels {
        ScheduleViewModelFactory()
    }

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

        // Load subject information
        loadSubjectInfor(viewModel.getSubjectById(subjectId = args.subjectId)!!)
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
        toolbar.inflateMenu(R.menu.day_schedule_fragment_menu)
        // Repair menu item
        toolbar.menu.removeItem(R.id.goToMenuItem)
        toolbar.menu.removeItem(R.id.addRemindMenuItem)
        toolbar.menu.findItem(R.id.refreshMenuItem).title = "Xóa"
        toolbar.menu.findItem(R.id.comeBackMenuItem).icon =
            ResourcesCompat.getDrawable(resources, R.drawable.baseline_edit_24, null)
        //set onclick for menu item
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.refreshMenuItem -> {
                    //todo xóa subject
                    viewModel.deleteSubject(args.subjectId)
                    val action =
                        DetailDayScheduleFragmentDirections.actionDetailDayScheduleFragmentToDayScheduleListFragment()
                    findNavControllerSafely()?.navigate(action)
                    Toast.makeText(context, "Delete subject", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.comeBackMenuItem -> {
                    //todo chuyển hướng đến edit fragment
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
     * @param subject
     */
    private fun loadSubjectInfor(subject: Subject) {
        binding.name.text = subject.name
        binding.time.text = String.format(
            "%s - %s\n%s",
            subject.timeStart,
            subject.timeEnd,
            viewModel.getDayScheduleById(subject.dayScheduleId)?.day ?: ""
        )

        if (subject.location.isEmpty()) {
            binding.location.visibility = View.GONE
        } else {
            binding.location.text = subject.location
        }

        if (subject.teacher.isEmpty()) {
            binding.teacher.visibility = View.GONE
        } else {
            binding.teacher.text = subject.teacher
        }

        if (subject.notes.isEmpty()) {
            binding.notes.visibility = View.GONE
        } else {
            binding.notes.text = subject.notes
        }
    }
}