package com.example.scheduleui.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.scheduleui.R
import com.example.scheduleui.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        setupToolbar()

        // Load settingFragment
        loadFragment()
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

    /**
     * This function is used to load SettingFragment
     */
    private fun loadFragment() {
        // Todo load SettingFragment
        binding.nameValue.text = getString(R.string.nameSettingDefault)
        binding.schoolValue.text = getString(R.string.nameSettingDefault)
        binding.typeOfScheduleValue.text = getString(R.string.firstTypeOfSchedule)
        binding.typeOfDisplayValue.text = getString(R.string.firstTypeOfDisplay)
        binding.displaySubjectValue.text = getString(R.string.displaySubjectOption1)
    }
}