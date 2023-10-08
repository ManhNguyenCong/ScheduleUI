package com.example.scheduleui.ui.setting.fragment

import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.scheduleui.R
import com.example.scheduleui.databinding.FragmentSettingBinding
import com.example.scheduleui.ui.setting.viewmodel.SettingViewModel
import com.example.scheduleui.ui.setting.viewmodel.SettingViewModelFactory

class SettingFragment : Fragment() {
    // Binding to fragment_setting
    private lateinit var binding: FragmentSettingBinding

    // View model
    private val viewModel: SettingViewModel by activityViewModels {
        SettingViewModelFactory(activity?.application as Application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        // Setup toolbar
        setupToolbar()
        // Load settingFragment
        loadFragment()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load content of this fragment
        loadContent()
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

        activity?.run {
            // Set dialog for name setting
            binding.nameValue.setOnClickListener {
                TextInputDialogFragment(
                    getString(R.string.nameSettingTitle),
                    binding.nameValue.text.toString()
                ) { text ->
                    // Save new name setting to DataStore
                    viewModel.setNewName(text)
                }
                    .show(supportFragmentManager, "TextInputDialogFragment")
            }
            // Set dialog for school setting
            binding.schoolValue.setOnClickListener {
                TextInputDialogFragment(
                    getString(R.string.schoolSettingTitle),
                    binding.schoolValue.text.toString()
                ) { text ->
                    //Save new school setting to DataStore
                    viewModel.setNewSchool(text)
                }.show(supportFragmentManager, "TextInputDialogFragment")
            }

        }

        // Event setup type of schedule
        binding.typeOfScheduleValue.setOnClickListener {
            // Get typeOfSchedule's list
            val typesOfSchedule = resources.getStringArray(R.array.typeOfSchedule)
            // Get index of current type
            var currentTypeIndex = typesOfSchedule.indexOf(binding.typeOfScheduleValue.text)

            // Create Builder
            val builder = AlertDialog.Builder(context)
            // Setup layout AlertDialog
            builder.setTitle(resources.getString(R.string.typeOfDisplayTitle))
                .setPositiveButton("Oke") { _, _ ->
                    // Save type of schedule to data store
                    viewModel.setNewTypeOfSchedule(typesOfSchedule[currentTypeIndex])
                }
                .setSingleChoiceItems(
                    resources.getStringArray(R.array.typeOfSchedule),
                    currentTypeIndex
                ) { _, index ->
                    // Re-setup to user's choose
                    currentTypeIndex = index
                }
            // Create dialog and show it
            builder.create().show()
        }
        // Event setup type of schedule display
        binding.typeOfDisplayValue.setOnClickListener {
            // Get typeOfDisplay's list
            val typesOfDisplay = resources.getStringArray(R.array.typeOfDisplay)
            // Get index of current type
            var currentTypeIndex = typesOfDisplay.indexOf(binding.typeOfDisplayValue.text)

            // Create dialog builder
            val builder = AlertDialog.Builder(context)
            // Setup dialog layout
            builder.setTitle(getString(R.string.typeOfDisplayTitle))
                .setPositiveButton("Oke") { _, _ ->
                    // Save type of schedule display to data store
                    viewModel.setNewTypeOfDisplay(typesOfDisplay[currentTypeIndex])
                }
                .setSingleChoiceItems(typesOfDisplay, currentTypeIndex) { _, index ->
                    // Re-setup to user' choose
                    currentTypeIndex = index
                }
            // Create dialog and show it
            builder.create().show()
        }
        // Event setup type of subject display
        binding.subjectDisplayValue.setOnClickListener {
            // Get types of subject display
            val typesOfDisplaySubject = resources.getStringArray(R.array.subjectDisplay)
            // Get index of current type
            var currentTypeIndex = typesOfDisplaySubject.indexOf(binding.subjectDisplayValue.text)

            // Create dialog builder
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.subjectDisplayTitle))
                .setPositiveButton("Oke") { _, _ ->
                    // Re-display type of subject display
                    viewModel.setNewSubjectDisplay(typesOfDisplaySubject[currentTypeIndex])
                }
                .setSingleChoiceItems(typesOfDisplaySubject, currentTypeIndex) { _, index ->
                    // Re-setup index of current type
                    currentTypeIndex = index
                }
            // Create dialog and show it
            builder.create().show()
        }
    }

    /**
     * This function is used to load content of SettingFragment
     */
    private fun loadContent() {
        // Get name setting from data store
        viewModel.nameSetting.observe(this.viewLifecycleOwner) { name ->
            if(name.isEmpty()) {
                // If name is empty, set it is "Not set"
                binding.nameValue.text = getString(R.string.nameSettingDefault)
            } else {
                // Set name
                binding.nameValue.text = name
            }
        }
        // Get school setting from data store
        viewModel.schoolSetting.observe(this.viewLifecycleOwner) { school ->
            if(school.isEmpty()) {
                // If school is empty, set school is "Not set"
                binding.schoolValue.text = getString(R.string.nameSettingDefault)
            } else {
                // Set school
                binding.schoolValue.text = school
            }
        }
        // Get type of schedule setting from data store
        viewModel.typeOfScheduleSetting.observe(this.viewLifecycleOwner) { typeOfSchedule ->
            if(typeOfSchedule.isEmpty()) {
                // If don's have save of it, set it is first default type
                binding.typeOfScheduleValue.text = resources.getStringArray(R.array.typeOfSchedule)[0]
            } else {
                binding.typeOfScheduleValue.text = typeOfSchedule
            }
        }
        // Get type of schedule display setting from data store
        viewModel.typeOfDisplaySetting.observe(this.viewLifecycleOwner) { typeOfDisplay ->
            if (typeOfDisplay.isEmpty()) {
                // If don't have save of schedule display's type, set it is first default type
                binding.typeOfDisplayValue.text = resources.getStringArray(R.array.typeOfDisplay)[0]
            } else {
                binding.typeOfDisplayValue.text =typeOfDisplay
            }
        }
        // Get type of subject display setting from data store
        viewModel.subjectDisplaySetting.observe(this.viewLifecycleOwner) { subjectDisplay ->
            if(subjectDisplay.isEmpty()) {
                // If don't have save of it, set it is first default type
                binding.subjectDisplayValue.text = resources.getStringArray(R.array.subjectDisplay)[0]
            } else {
                // Set type of subject display
                binding.subjectDisplayValue.text = subjectDisplay
            }
        }
    }
}

/**
 * Customer Dialog with EditText
 *
 * @param title title of dialog
 * @param currentValue current value of view
 * @param positiveButtonClick event positive button click
 */
class TextInputDialogFragment(
    private val title: String,
    private val currentValue: String,
    private val positiveButtonClick: (String) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            // Create dialog builder
            val builder = AlertDialog.Builder(context)
            // Inflate layout for dialog
            val dialogLayout = layoutInflater.inflate(R.layout.text_input_dialog_layout, null)
            // Get edit text
            val editText = dialogLayout.findViewById<EditText>(R.id.textInput)
            // Set text of current value
            editText.setText(currentValue)
            // Set layout for dialog
            builder.setView(dialogLayout)
                .setTitle(title)
                .setPositiveButton("Oke") { _, _ ->
                    positiveButtonClick(editText.text.toString())
                }

            // Create dialog
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}