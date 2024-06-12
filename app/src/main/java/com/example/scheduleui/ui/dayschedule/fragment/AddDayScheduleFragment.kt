package com.example.scheduleui.ui.dayschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.scheduleui.databinding.FragmentAddDayScheduleBinding

class AddDayScheduleFragment : Fragment() {
//    // Argument of AddDayScheduleFragment in nav_graph
//    private val args: AddDayScheduleFragmentArgs by navArgs()

    // Binding to view FragmentAddDaySchedule
    private lateinit var binding: FragmentAddDayScheduleBinding
//    private var targetDay: Calendar? = null
//
//    // Wait time for add day schedule
//    private var isWaitTime = false
//        set(value) {
//            field = value
//            if (value) {
//                checkAutoAddCompleted(targetDay ?: Calendar.getInstance().setDefaultTime())
//                Toast.makeText(context, "Wait a few seconds ...", Toast.LENGTH_SHORT).show()
//            }
//            if (isWaitTime) {
//                binding.btnDayStart.isEnabled = false
//                binding.btnDayEnd.isEnabled = false
//            } else {
//                binding.btnDayStart.isEnabled = true
//                binding.btnDayEnd.isEnabled = true
//            }
//        }

//    // View model
//    private val viewModel: DayScheduleViewModel by activityViewModels {
//        DayScheduleViewModelFactory(
//            (activity?.application as ScheduleApplication).database.scheduleDao()
//        )
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddDayScheduleBinding.inflate(inflater, container, false)

//        // Setup toolbar
//        setupToolbar()
//        // Load fragment
//        loadFragment()

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Load content
//        loadContent()
//    }
//
//    /**
//     * This function is setup menu and menuItemSelected for AddDayScheduleFragment
//     */
//    private fun setupToolbar() {
//        // Get toolbar form activity
//        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar) ?: return
//
//        // Clean old menu
//        toolbar.menu.clear()
//        // Inflate new menu
//        toolbar.inflateMenu(R.menu.add_fragment_menu)
//        //Set onclick for menu item
//        toolbar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.saveMenuItem -> {
//                    if(args.subjectId != -1) {
//                        updateSubject()
//                    } else {
//                        addNewSubjects()
//                    }
//                    true
//                }
//
//                else -> false
//            }
//        }
//    }
//
//    /**
//     * This function is used to load add fragment or edit fragment
//     *
//     */
//    private fun loadFragment() {
//
//        // Setup event btnTimeStart click
//        binding.btnTimeStart.setOnClickListener {
//            // Get current time start
//            val currentTime = binding.btnTimeStart.text.toString().getTimeFromTimeString()
//            // Initialize time picker dialog
//            val timePickerDialog = TimePickerDialog(
//                requireContext(),
//                { _, hourOfDay, minute ->
//                    // Event choose time start
//                    binding.btnTimeStart.text =
//                        Calendar.Builder().setTimeOfDay(hourOfDay, minute, 0).build()
//                            .formatDayScheduleTime()
//
//                    // If timeEnd <= timeStart, set timeEnd = timeStart + 60m
//                    val timeStart = binding.btnTimeStart.text.toString().getTimeFromTimeString()
//                    val timeEnd = binding.btnTimeEnd.text.toString().getTimeFromTimeString()
//                    if (!viewModel.validTimeEntry(timeStart, timeEnd)) {
//                        binding.btnTimeEnd.text = Calendar.Builder()
//                            .setInstant(timeStart.timeInMillis + DayScheduleViewModel.DAY_TO_MILLIS / 24)
//                            .build().formatDayScheduleTime()
//                    }
//                },
//                currentTime.get(Calendar.HOUR_OF_DAY),
//                currentTime.get(Calendar.MINUTE),
//                true
//            )
//            // Show time picker dialog
//            timePickerDialog.show()
//        }
//
//        // Setup event btnTimeEnd click
//        binding.btnTimeEnd.setOnClickListener {
//            // Get current time end
//            val currentTime = binding.btnTimeEnd.text.toString().getTimeFromTimeString()
//            // Initialize time picker dialog
//            val timePickerDialog = TimePickerDialog(
//                requireContext(),
//                { _, hourOfDay, minute ->
//                    // Event choose time end
//                    binding.btnTimeEnd.text =
//                        Calendar.Builder().setTimeOfDay(hourOfDay, minute, 0).build()
//                            .formatDayScheduleTime()
//
//                    // If timeEnd <= timeStart, set timeEnd = timeStart + 60m
//                    val timeStart = binding.btnTimeStart.text.toString().getTimeFromTimeString()
//                    val timeEnd = binding.btnTimeEnd.text.toString().getTimeFromTimeString()
//                    if (!viewModel.validTimeEntry(timeStart, timeEnd)) {
//                        binding.btnTimeEnd.text = Calendar.Builder()
//                            .setInstant(timeStart.timeInMillis + DayScheduleViewModel.DAY_TO_MILLIS / 24)
//                            .build().formatDayScheduleTime()
//                    }
//                },
//                currentTime.get(Calendar.HOUR_OF_DAY),
//                currentTime.get(Calendar.MINUTE),
//                true
//            )
//            // Show time picker dialog
//            timePickerDialog.show()
//        }
//
//        // Setup event btnDayStart click
//        binding.btnDayStart.setOnClickListener {
//            // Get current day start
//            val currentDate = binding.btnDayStart.text.toString().getDateFromString()
//            // Initialize date picker dialog
//            val datePickerDialog = DatePickerDialog(
//                requireContext(),
//                { _, year, month, dayOfMonth ->
//                    // Event choose date start
//                    binding.btnDayStart.text =
//                        Calendar.Builder().setDate(year, month, dayOfMonth).build()
//                            .formatDayScheduleDate()
//
//                    // If dayEnd < dayStart, set dayEnd = dayStart
//                    if (!viewModel.validDateEntry(
//                            binding.btnDayStart.text.toString().getDateFromString(),
//                            binding.btnDayEnd.text.toString().getDateFromString()
//                        )
//                    ) {
//                        binding.btnDayEnd.text = binding.btnDayStart.text
//                    }
//
//                    isWaitTime = true
//                    targetDay = binding.btnDayStart.text.toString().getDateFromString()
//                },
//                currentDate.get(Calendar.YEAR),
//                currentDate.get(Calendar.MONTH),
//                currentDate.get(Calendar.DAY_OF_MONTH)
//            )
//            // Show date picker dialog
//            datePickerDialog.show()
//
//
//        }
//
//        // Setup event btnDayEnd click
//        binding.btnDayEnd.setOnClickListener {
//            // Get current day end
//            val currentDate = binding.btnDayEnd.text.toString().getDateFromString()
//            // Initialize date picker dialog
//            val datePickerDialog = DatePickerDialog(
//                requireContext(),
//                { _, year, month, dayOfMonth ->
//                    // Event choose date end
//                    binding.btnDayEnd.text =
//                        Calendar.Builder().setDate(year, month, dayOfMonth).build()
//                            .formatDayScheduleDate()
//
//                    // If dayEnd < dayStart, set dayEnd = dayStart
//                    if (!viewModel.validDateEntry(
//                            binding.btnDayStart.text.toString().getDateFromString(),
//                            binding.btnDayEnd.text.toString().getDateFromString()
//                        )
//                    ) {
//                        binding.btnDayEnd.text = binding.btnDayStart.text
//                    }
//
//                    isWaitTime = true
//                    targetDay = binding.btnDayEnd.text.toString().getDateFromString()
//                },
//                currentDate.get(Calendar.YEAR),
//                currentDate.get(Calendar.MONTH),
//                currentDate.get(Calendar.DAY_OF_MONTH)
//            )
//            // Show date picker dialog
//            datePickerDialog.show()
//        }
//
//        // Set up loop
//        setupLoop()
//    }
//
//    /**
//     * This function is used to load content of this fragment
//     */
//    private fun loadContent() {
//        if (args.subjectId != -1) {
//            // Load edit fragment
//            viewModel.getSubjectById(args.subjectId).observe(this.viewLifecycleOwner) { subject ->
//                loadEditFragment(subject)
//            }
//        } else {
//            // Load add fragment
//            // Set btnDayStart, btnDayEnd
//            binding.btnDayStart.text =
//                args.date.ifEmpty { Calendar.getInstance().formatDayScheduleDate() }
//            binding.btnDayEnd.text =
//                args.date.ifEmpty { Calendar.getInstance().formatDayScheduleDate() }
//            //Set btnTimeStart, btnTimeEnd
//            binding.btnTimeStart.text = Calendar.getInstance().formatDayScheduleTime()
//            binding.btnTimeEnd.text =
//                Calendar.Builder()
//                    .setInstant(Calendar.getInstance().timeInMillis + DayScheduleViewModel.DAY_TO_MILLIS / 24)
//                    .build()
//                    .formatDayScheduleTime()
//        }
//    }
//
//    /**
//     * This function is used to set up loop content when has a check box changed
//     */
//    private fun setupLoop() {
//        binding.cbxCN.setOnClickListener {
//            setupDisplayLoopContent()
//        }
//        binding.cbxT2.setOnClickListener {
//            setupDisplayLoopContent()
//        }
//        binding.cbxT3.setOnClickListener {
//            setupDisplayLoopContent()
//        }
//        binding.cbxT4.setOnClickListener {
//            setupDisplayLoopContent()
//        }
//        binding.cbxT5.setOnClickListener {
//            setupDisplayLoopContent()
//        }
//        binding.cbxT6.setOnClickListener {
//            setupDisplayLoopContent()
//        }
//        binding.cbxT7.setOnClickListener {
//            setupDisplayLoopContent()
//        }
//    }
//
//    /**
//     * This function is used to check this subject has looped
//     */
//    private fun getDayOfWeeksLoop(): String {
//        val dayOfWeeksLoop = mutableListOf<Int>()
//
//        if (binding.cbxCN.isChecked) dayOfWeeksLoop.add(0)
//        if (binding.cbxT2.isChecked) dayOfWeeksLoop.add(1)
//        if (binding.cbxT3.isChecked) dayOfWeeksLoop.add(2)
//        if (binding.cbxT4.isChecked) dayOfWeeksLoop.add(3)
//        if (binding.cbxT5.isChecked) dayOfWeeksLoop.add(4)
//        if (binding.cbxT6.isChecked) dayOfWeeksLoop.add(5)
//        if (binding.cbxT7.isChecked) dayOfWeeksLoop.add(6)
//
//        return dayOfWeeksLoop.joinToString(", ")
//    }
//
//    /**
//     * This function is used to set up loop content display in screen
//     */
//    private fun setupDisplayLoopContent() {
//        val dayOfWeeks = getDayOfWeeksLoop().split(", ").map {
//            resources.getStringArray(R.array.dayOfWeeks)[it.toInt()]
//        }.joinToString(", ")
//
//        if (dayOfWeeks.isEmpty()) {
//            binding.loopContent.visibility = View.GONE
//        } else {
//            binding.loopContent.text = String.format(
//                getString(R.string.loopContentTemplate),
//                dayOfWeeks,
//                binding.btnTimeStart.text.toString(),
//                binding.btnTimeEnd.text.toString(),
//                binding.btnDayStart.text.toString(),
//                binding.btnDayEnd.text.toString(),
//            )
//            binding.loopContent.visibility = View.VISIBLE
//        }
//    }
//
//    /**
//     * This function is used to set loop content for a subject
//     */
//    private fun setLoopContent(): String {
//        // Get dayOfWeeks, this subject loop
//        val dayOfWeeks = getDayOfWeeksLoop()
//
//        return if (dayOfWeeks.isEmpty()) {
//            ""
//        } else {
//            // Format loop content
//            String.format(
//                "%s-%s-%s",
//                dayOfWeeks,
//                binding.btnDayStart.text.toString(),
//                binding.btnDayEnd.text.toString(),
//            )
//        }
//    }
//
//    /**
//     * This function is used to valid subject entry
//     */
//    private fun isEntryValid(): Boolean {
//        return viewModel.isEntryValid(binding.txtName.text.toString())
//    }
//
//    /**
//     * This function is used to add new a subject without loop content
//     */
//    private fun addNewASubject() {
//        viewModel.addNewASubject(
//            binding.txtName.text.toString(),
//            binding.btnTimeStart.text.toString(),
//            binding.btnTimeEnd.text.toString(),
//            binding.txtLocation.text.toString(),
//            binding.txtTeacher.text.toString(),
//            binding.txtNotes.text.toString(),
//            binding.loopContent.text.toString(),
//            binding.btnDayStart.text.toString()
//        )
//    }
//
//    /**
//     * This function is used to add new subjects with the same loop content,
//     * or add new a subject without loop content
//     *
//     */
//    private fun addNewSubjects() {
//        if (isEntryValid()) {
//            // Check this subject loop?
//            if (binding.loopContent.visibility == View.GONE) { // Don't loop
//                // Add new a subject
//                addNewASubject()
//            } else if (binding.loopContent.visibility == View.VISIBLE) { // Loop
//                viewModel.addNewSubjects(
//                    binding.txtName.text.toString(),
//                    binding.btnTimeStart.text.toString(),
//                    binding.btnTimeEnd.text.toString(),
//                    binding.txtLocation.text.toString(),
//                    binding.txtTeacher.text.toString(),
//                    binding.txtNotes.text.toString(),
//                    setLoopContent(),
//                    binding.btnDayStart.text.toString(),
//                    binding.btnDayEnd.text.toString()
//                )
//            }
//            // Navigate to DayScheduleListFragment
//            val action =
//                AddDayScheduleFragmentDirections.actionAddDayScheduleFragmentToDayScheduleListFragment()
//            findNavControllerSafely()?.navigate(action)
//        } else {
//            Toast.makeText(context, "Entry subject name!", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    /**
//     * This function is used to check "auto add day schedule is completed?"
//     */
//    private fun checkAutoAddCompleted(targetDay: Calendar) {
//        viewModel.daySchedules.observe(this.viewLifecycleOwner) { daySchedules ->
//            if (isWaitTime) {
//                // Check auto add completed?
//                isWaitTime = !viewModel.checkAutoAddDayCompleted(
//                    daySchedules,
//                    targetDay
//                )
//            }
//        }
//    }
//
//    /**
//     * This function is used to load edit fragment
//     */
//    private fun loadEditFragment(subject: Subject) {
//        // Set subject name
//        binding.txtName.setText(subject.name)
//        // Set timeStart, timeEnd
//        binding.btnTimeStart.text = subject.timeStart.formatDayScheduleTime()
//        binding.btnTimeEnd.text = subject.timeEnd.formatDayScheduleTime()
//        // Set txtLocation, txtTeacher, txtNotes
//        binding.txtLocation.setText(subject.location)
//        binding.txtTeacher.setText(subject.teacher)
//        binding.txtNotes.setText(subject.notes)
//        // Set day
//        viewModel.daySchedules.observe(this.viewLifecycleOwner) { daySchedules ->
//            binding.btnDayStart.text = daySchedules.find {
//                it.id == subject.dayScheduleId
//            }?.day?.formatDayScheduleDate() ?: ""
//        }
//        // You can't edit day have this subject
//        binding.btnDayStart.isEnabled = false
//        binding.btnDayEnd.visibility = View.GONE
//
//        // After subjects was saved in local database, they will become different subjects,
//        // so don't edit loop content of them
//        binding.loopTitle.visibility = View.GONE
//        binding.dayLoopLayout.visibility = View.GONE
//        binding.line3.visibility = View.GONE
//    }
//
//    /**
//     * This function is used to update a subject
//     */
//    private fun updateSubject() {
//        if(isEntryValid()) {
//            // Update subject
//            viewModel.updateSubject(
//                args.subjectId,
//                binding.txtName.text.toString(),
//                binding.btnTimeStart.text.toString(),
//                binding.btnTimeEnd.text.toString(),
//                binding.txtLocation.text.toString(),
//                binding.txtTeacher.text.toString(),
//                binding.txtNotes.text.toString(),
//                binding.loopContent.text.toString(),
//                binding.btnDayStart.text.toString()
//            )
//
//            // Navigate to DayScheduleListFragment
//            val action =
//                AddDayScheduleFragmentDirections.actionAddDayScheduleFragmentToDayScheduleListFragment()
//            findNavControllerSafely()?.navigate(action)
//        } else {
//            Toast.makeText(context, "Entry subject name!", Toast.LENGTH_SHORT).show()
//        }
//    }
}