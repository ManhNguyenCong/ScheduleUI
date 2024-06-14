package com.example.scheduleui.ui.setting.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.scheduleui.R
import com.example.scheduleui.ScheduleApplication
import com.example.scheduleui.data.localdatabase.SettingPreferences
import com.example.scheduleui.data.repository.NotifRepository
import com.example.scheduleui.data.repository.SubjectRepository
import com.example.scheduleui.databinding.FragmentSettingBinding
import com.example.scheduleui.ui.DayScheduleViewModel
import com.example.scheduleui.ui.DayScheduleViewModelFactory

class SettingFragment : Fragment() {
    // Binding to fragment_setting
    private lateinit var binding: FragmentSettingBinding

    private val viewModel: DayScheduleViewModel by activityViewModels {
        DayScheduleViewModelFactory(
            SubjectRepository((activity?.application as ScheduleApplication).database.scheduleDao()),
            NotifRepository((activity?.application as ScheduleApplication).database.scheduleDao()),
            SettingPreferences(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        // Setup toolbar
        setupToolbar()

        binding.nameValue.setOnClickListener {
            TextInputDialogFragment(
                getString(R.string.nameSettingTitle),
                binding.nameValue.text.toString()
            ) { text ->
                // Save new name setting to DataStore
                viewModel.setNameSetting(text)
            }.show(parentFragmentManager, TextInputDialogFragment.TAG)
        }

        binding.schoolValue.setOnClickListener {
            TextInputDialogFragment(
                getString(R.string.schoolSettingTitle),
                binding.schoolValue.text.toString()
            ) { text ->
                //Save new school setting to DataStore
                viewModel.setSchoolSetting(text)
            }.show(parentFragmentManager, TextInputDialogFragment.TAG)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNameSetting().observe(this.viewLifecycleOwner) {
            binding.nameValue.text = if (it.isNullOrEmpty()) {
                getString(R.string.nameSettingDefault)
            } else {
                it
            }
        }

        viewModel.getSchoolSetting().observe(this.viewLifecycleOwner) {
            binding.schoolValue.text = if (it.isNullOrEmpty()) {
                getString(R.string.nameSettingDefault)
            } else {
                it
            }
        }
    }

    /**
     * This function is used to clean menu for SettingFragment
     */
    private fun setupToolbar() {
        // Get toolbar for activity
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar) ?: return

        // Clean menu
        toolbar.menu.clear()
    }
}

