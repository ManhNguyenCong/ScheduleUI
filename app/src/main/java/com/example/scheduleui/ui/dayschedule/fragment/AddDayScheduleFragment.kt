package com.example.scheduleui.ui.dayschedule.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.scheduleui.R
import com.example.scheduleui.databinding.FragmentAddDayScheduleBinding
import com.example.scheduleui.findNavControllerSafely
import com.example.scheduleui.ui.viewmodel.ScheduleViewModel
import com.example.scheduleui.ui.viewmodel.ScheduleViewModelFactory
import java.util.Calendar

class AddDayScheduleFragment : Fragment() {

    private val args: AddDayScheduleFragmentArgs by navArgs()

    private lateinit var binding: FragmentAddDayScheduleBinding

    private val viewModel: ScheduleViewModel by activityViewModels {
        ScheduleViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddDayScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        setupToolbar()

        // Load fragment
        loadFragment()
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
        toolbar.inflateMenu(R.menu.add_day_schedule_menu)
        //Set onclick for menu item
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.saveMenuItem -> {
                    //Todo Save new Subject in DaySchedule

                    val action = AddDayScheduleFragmentDirections.actionAddDayScheduleFragmentToDayScheduleListFragment()
                    findNavControllerSafely()?.navigate(action)

                    Toast.makeText(context, "Thêm/sửa DaySchedule", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }

    /**
     * This function is used to load add fragment or edit fragment
     *
     */
    private fun loadFragment() {

        if (args.subjectId != -1) {
            //todo load edit fragment
            val subject = viewModel.getSubjectById(args.subjectId) ?: return

            if (subject.loop.isEmpty()) {
                // Nếu không lặp thì ẩn loopContent đi
                binding.loopContent.visibility = View.GONE

                // Lấy dayStart và gán vào btnDayEnd và btnDayStart
                var dayStart = viewModel.getDayScheduleById(subject.id)!!.day
                dayStart = dayStart.substring(dayStart.indexOf(",") + 1).trim()

                binding.btnDayEnd.text = dayStart
                binding.btnDayStart.text = dayStart
            } else {
                //todo xử lý trường hợp có lặp

                // Chuỗi loop trong subject có template: dayOfWeeks-dayStart-dayEnd
                val strings = subject.loop.split("-")

                // Trong dayOfWeeks các thứ được ngăn cách bởi ", "
                strings[0].split(", ").forEach { dayOfWeek ->
                    when(dayOfWeek) {
                        "Chủ nhật" -> binding.cbxCN.isChecked = true
                        "Thứ hai" -> binding.cbxT2.isChecked = true
                        "Thứ ba" -> binding.cbxT3.isChecked = true
                        "Thứ tư" -> binding.cbxT4.isChecked = true
                        "Thứ năm" -> binding.cbxT5.isChecked = true
                        "Thứ sáu" -> binding.cbxT6.isChecked = true
                        else -> binding.cbxT7.isChecked = true
                    }
                }

                // Gán dayEnd và dayStart tương ứng
                binding.btnDayStart.text = strings[1]
                binding.btnDayEnd.text = strings[2]
            }

            // Gán tên môn
            binding.txtName.setText(subject.name)

            // Gán timeStart và timeEnd
            binding.btnTimeStart.text = subject.timeStart
            binding.btnTimeEnd.text = subject.timeEnd

            // Gán các thuộc tính location, teacher, notes
            binding.txtLocation.setText(subject.location)
            binding.txtTeacher.setText(subject.teacher)
            binding.txtNotes.setText(subject.notes)
        }

        // Load loop content
        setupLoop()

        // Set up btn dayStart and dayEnd
        setupButtonDatePicker(binding.btnDayStart)
        setupButtonDatePicker(binding.btnDayEnd)

        // Set up btn timeStart and timeEnd
        setupButtonTimePicker(binding.btnTimeStart)
        setupButtonTimePicker(binding.btnTimeEnd)
    }

    /**
     * This function is used to set up a button which is used to choose time of day
     */
    private fun setupButtonTimePicker(button: Button) {
        //todo xử lý button timePicked

        // Nếu button chưa có text thì set text là thời gian hiện tại
        if (button.text.isEmpty()) {
            button.text = viewModel.getStringTimeFromTime(
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE)
            )
        }

        val timePicked = viewModel.getTimeFromStringTime(button.text.toString())
        button.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    button.text = String.format("%2d:%2d", hour, minute)

                    // Kiểm tra tính hợp lệ của thời gian được nhập
                    if(!viewModel.validTimeEntry(
                            viewModel.getTimeFromStringTime(binding.btnTimeStart.text.toString()),
                            viewModel.getTimeFromStringTime(binding.btnTimeEnd.text.toString())
                        )) {
                        binding.btnTimeEnd.text = binding.btnTimeStart.text
                    }
                },
                timePicked.get(Calendar.HOUR_OF_DAY),
                timePicked.get(Calendar.MINUTE),
                true
            )

            timePickerDialog.show()
        }
    }

    /**
     * This function is used to set up a button which is used to choose date/month/year
     */
    private fun setupButtonDatePicker(button: Button) {
        //todo xử lý button datePicker

        if (button.text.isEmpty()) {
            button.text = viewModel.getStringDateFromDate(
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.YEAR)
            )
        }

        val datePicked = viewModel.getDateFromStringDate(button.text.toString())
        button.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, date ->
                    button.text = viewModel.getStringDateFromDate(date, month, year)

                    // Kiểm tra tính hợp lệ của ngày được nhập
                    if(!viewModel.validDateEntry(
                            viewModel.getDateFromStringDate(binding.btnDayStart.text.toString()),
                            viewModel.getDateFromStringDate(binding.btnDayEnd.text.toString())
                        )) {
                        binding.btnDayStart.text = binding.btnDayEnd.text
                    }
                },
                datePicked.get(Calendar.YEAR),
                datePicked.get(Calendar.MONTH),
                datePicked.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }
    }

    /**
     * This function is used to set up loop content when has a check box changed
     */
    private fun setupLoop() {
        binding.cbxCN.setOnClickListener {
            setupLoopContent()
        }
        binding.cbxT2.setOnClickListener {
            setupLoopContent()
        }
        binding.cbxT3.setOnClickListener {
            setupLoopContent()
        }
        binding.cbxT4.setOnClickListener {
            setupLoopContent()
        }
        binding.cbxT5.setOnClickListener {
            setupLoopContent()
        }
        binding.cbxT6.setOnClickListener {
            setupLoopContent()
        }
        binding.cbxT7.setOnClickListener {
            setupLoopContent()
        }

    }

    /**
     * This function is used to check this subject has looped
     */
    private fun getDayOfWeeksLoop(): String {
        val dayOfWeeksLoop = mutableListOf<String>()
        if (binding.cbxCN.isChecked) dayOfWeeksLoop.add("Chủ nhật")
        if (binding.cbxT2.isChecked) dayOfWeeksLoop.add("Thứ hai")
        if (binding.cbxT3.isChecked) dayOfWeeksLoop.add("Thứ ba")
        if (binding.cbxT4.isChecked) dayOfWeeksLoop.add("Thứ tư")
        if (binding.cbxT5.isChecked) dayOfWeeksLoop.add("Thứ năm")
        if (binding.cbxT6.isChecked) dayOfWeeksLoop.add("Thứ sáu")
        if (binding.cbxT7.isChecked) dayOfWeeksLoop.add("Thứ bảy")

        return dayOfWeeksLoop.joinToString(", ")
    }

    /**
     * This function is used to set up loop content
     */
    private fun setupLoopContent() {
        val dayOfWeeks = getDayOfWeeksLoop()

        if (dayOfWeeks.isEmpty()) {
            binding.loopContent.visibility = View.GONE
        } else {
            binding.loopContent.text = String.format(
                getString(R.string.loopContentTemplate),
                dayOfWeeks,
                binding.btnTimeStart.text.toString(),
                binding.btnTimeEnd.text.toString(),
                binding.btnDayStart.text.toString(),
                binding.btnDayEnd.text.toString(),
            )
            binding.loopContent.visibility = View.VISIBLE
        }
    }


}