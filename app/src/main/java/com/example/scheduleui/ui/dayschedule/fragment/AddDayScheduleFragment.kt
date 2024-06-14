package com.example.scheduleui.ui.dayschedule.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.example.scheduleui.databinding.FragmentAddDayScheduleBinding
import com.example.scheduleui.ui.DayScheduleViewModel
import com.example.scheduleui.ui.DayScheduleViewModelFactory
import com.example.scheduleui.util.findNavControllerSafely
import com.example.scheduleui.util.format
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class AddDayScheduleFragment : Fragment() {
    // Argument of AddDayScheduleFragment in nav_graph
    private val args: AddDayScheduleFragmentArgs by navArgs()

    // Binding to view FragmentAddDaySchedule
    private lateinit var binding: FragmentAddDayScheduleBinding

    private var timeStart: LocalTime = LocalTime.now()
        set(value) {
            field = value
            if (!field.isBefore(timeEnd)) {
                timeEnd = field.plusHours(1)
            }
            binding.btnTimeStart.text = timeStart.format("HH:mm")
        }
    private var timeEnd: LocalTime = timeStart.plusHours(1)
        set(value) {
            if (value.isAfter(timeStart)) {
                field = value
                binding.btnTimeEnd.text = timeEnd.format("HH:mm")
            } else {
                Toast.makeText(
                    requireContext(),
                    "Thời gian kết thúc phải sau thời gian bắt đầu!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private var dateStart: LocalDate = LocalDate.now()
        set(value) {
            field = value
            if (field.isAfter(dateEnd)) {
                dateEnd = field
            }
            binding.btnDayStart.text = field.format("cccc, dd/MM/yyyy")
        }
    private var dateEnd: LocalDate = dateStart
        set(value) {
            if (!value.isBefore(dateStart)) {
                field = value
                binding.btnDayEnd.text = dateEnd.format("cccc, dd/MM/yyyy")
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ngày kết thúc không thể trước ngày bắt đầu!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private var weekDaysLoop = booleanArrayOf(false, false, false, false, false, false, false)
        set(value) {
            field = value
            if (!field.contains(true)) {
                binding.cbxLoop.isChecked = false
                binding.btnDayEnd.isEnabled = false
                binding.loopContent.visibility = View.GONE
            } else if (binding.cbxLoop.isChecked) {
                binding.loopContent.visibility = View.VISIBLE
                binding.loopContent.text = String.format(
                    getString(R.string.loopContentTemplate),
                    field.mapIndexed { index, b ->
                        if (b) {
                            when (index) {
                                1 -> DayOfWeek.MONDAY.getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.getDefault()
                                )

                                2 -> DayOfWeek.TUESDAY.getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.getDefault()
                                )

                                3 -> DayOfWeek.WEDNESDAY.getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.getDefault()
                                )

                                4 -> DayOfWeek.THURSDAY.getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.getDefault()
                                )

                                5 -> DayOfWeek.FRIDAY.getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.getDefault()
                                )

                                6 -> DayOfWeek.SATURDAY.getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.getDefault()
                                )

                                else -> DayOfWeek.SUNDAY.getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.getDefault()
                                )
                            }
                        } else null
                    }.filterNotNull().joinToString(", "),
                    timeStart.format("HH:mm"),
                    timeEnd.format("HH:mm"),
                    dateStart.format("dd/MM/yyyy"),
                    dateEnd.format("dd/MM/yyyy")
                )
            }
        }

    private var subject: Subject? = null

    // View model
    private val viewModel: DayScheduleViewModel by activityViewModels {
        DayScheduleViewModelFactory(
            SubjectRepository((activity?.application as ScheduleApplication).database.scheduleDao()),
            NotifRepository((activity?.application as ScheduleApplication).database.scheduleDao()),
            SettingPreferences(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddDayScheduleBinding.inflate(inflater, container, false)

        // Setup toolbar
        setupToolbar()

        if (args.subjectId != -1) {
            binding.btnDayEnd.visibility = View.GONE
            binding.loopTitle.visibility = View.GONE
            binding.cbxLoop.visibility = View.GONE
            binding.line2.visibility = View.GONE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.subjectId != -1) {
            viewModel.getSubjectById(args.subjectId).observe(this.viewLifecycleOwner) { subject ->
                this.subject = subject
                with(subject) {
                    binding.txtName.setText(name)
                    this@AddDayScheduleFragment.timeStart = timeStart
                    this@AddDayScheduleFragment.timeEnd = timeEnd
                    this@AddDayScheduleFragment.dateStart = date
                    binding.txtTeacher.setText(teacher)
                    binding.txtLocation.setText(location)
                    binding.txtNotes.setText(notes)
                }
            }
        } else {
            if (!args.date.isNullOrEmpty()) {
                dateStart = LocalDate.parse(
                    args.date, DateTimeFormatter.ISO_LOCAL_DATE
                )
                dateEnd = dateStart
            }

            setUpBtnTime()
            setUpBtnDate()
            setUpLoopOption()
        }
    }

    private fun setUpLoopOption() {
        binding.cbxLoop.setOnClickListener {
            binding.btnDayEnd.isEnabled = binding.cbxLoop.isChecked
            if (!binding.cbxLoop.isChecked) {
                weekDaysLoop = booleanArrayOf(false, false, false, false, false, false, false)
            } else {
                if (!weekDaysLoop.contains(true)) {
                    weekDaysLoop[dateStart.dayOfWeek.value % 7] = true
                }

                val itemsSelected = weekDaysLoop
                AlertDialog.Builder(requireContext())
                    .setTitle("Lựa chọn lặp")
                    .setMultiChoiceItems(
                        resources.getStringArray(R.array.dayOfWeeks),
                        itemsSelected
                    ) { _, i, isChecked ->
                        itemsSelected[i] = isChecked
                    }
                    .setPositiveButton("Xác nhận") { _, _ ->
                        weekDaysLoop = itemsSelected
                    }
                    .setNegativeButton("Quay lại") { _, _ ->
                        binding.cbxLoop.isChecked = false
                        weekDaysLoop =
                            booleanArrayOf(false, false, false, false, false, false, false)
                    }
                    .create()
                    .show()
            }
        }
    }

    private fun setUpBtnDate() {
        binding.btnDayStart.text = dateStart.format("cccc, dd/MM/yyyy")
        binding.btnDayStart.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                requireContext(), { datePicker, _, _, _ ->
                    dateStart =
                        LocalDate.of(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth)
                }, dateStart.year, dateStart.monthValue - 1, dateStart.dayOfMonth
            )
            datePickerDialog.create()
            datePickerDialog.show()
        }

        binding.btnDayEnd.text = dateEnd.format("cccc, dd/MM/yyyy")
        binding.btnDayEnd.isEnabled = false
        binding.btnDayEnd.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(), { datePicker, _, _, _ ->
                    dateEnd =
                        LocalDate.of(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth)
                }, dateEnd.year, dateEnd.monthValue - 1, dateEnd.dayOfMonth
            )
            datePickerDialog.create()
            datePickerDialog.show()
        }
    }

    private fun setUpBtnTime() {
        binding.btnTimeStart.text = timeStart.format("HH:mm")
        binding.btnTimeStart.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(), { timePicker, _, _ ->
                    timeStart = LocalTime.of(timePicker.hour, timePicker.minute)
                }, timeStart.hour, timeStart.minute, true
            )
            timePickerDialog.create()
            timePickerDialog.show()
        }

        binding.btnTimeEnd.text = timeEnd.format("HH:mm")
        binding.btnTimeEnd.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(), { timePicker, _, _ ->
                    timeEnd = LocalTime.of(timePicker.hour, timePicker.minute)
                }, timeEnd.hour, timeEnd.minute, true
            )
            timePickerDialog.create()
            timePickerDialog.show()
        }
    }

    /**
     * This function is setup menu and menuItemSelected for AddDayScheduleFragment
     */
    private fun setupToolbar() {
        // Get toolbar form activity
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar) ?: return

        // Clean old menu
        toolbar.menu.clear()
        // Inflate new menu
        toolbar.inflateMenu(R.menu.add_fragment_menu)
        //Set onclick for menu item
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.saveMenuItem -> {
                    if (args.subjectId != -1) {
                        update()
                    } else {
                        create()
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun create() {
        val name = binding.txtName.text?.toString()?.trim()
        val teacher = binding.txtTeacher.text.toString().trim()
        val location = binding.txtLocation.text.toString().trim()
        val notes = binding.txtNotes.text.toString().trim()

        if (name.isNullOrEmpty()) {
            binding.txtName.error = "Không được để trống!"
            return
        } else {
            binding.txtName.error = null
        }

        if (!timeEnd.isAfter(timeStart)) {
            Toast.makeText(
                requireContext(),
                "Thời gian kết thúc phải sau thời gian bắt đầu!!!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewModel.insert(
            name,
            timeStart,
            timeEnd,
            dateStart,
            dateEnd,
            teacher,
            location,
            notes,
            weekDaysLoop
        )
        findNavControllerSafely()?.navigateUp()
    }

    private fun update() {
        val name = binding.txtName.text?.toString()?.trim()
        val teacher = binding.txtTeacher.text.toString().trim()
        val location = binding.txtLocation.text.toString().trim()
        val notes = binding.txtNotes.text.toString().trim()

        if (name.isNullOrEmpty()) {
            binding.txtName.error = "Không được để trống!"
            return
        } else {
            binding.txtName.error = null
        }

        if (!timeEnd.isAfter(timeStart)) {
            Toast.makeText(
                requireContext(),
                "Thời gian kết thúc phải sau thời gian bắt đầu!!!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        subject?.let {
            viewModel.update(
                it.copy(
                    name = name,
                    timeStart = timeStart,
                    timeEnd = timeEnd,
                    date = dateStart,
                    location = location,
                    teacher = teacher,
                    notes = notes
                )
            )
            findNavControllerSafely()?.navigateUp()
        } ?: Toast.makeText(requireContext(), "Hiện có lỗi xảy ra...", Toast.LENGTH_SHORT).show()
    }
}