package com.example.scheduleui.ui.notification.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.scheduleui.R
import com.example.scheduleui.ScheduleApplication
import com.example.scheduleui.data.localdatabase.SettingPreferences
import com.example.scheduleui.data.model.Notification
import com.example.scheduleui.data.repository.NotifRepository
import com.example.scheduleui.data.repository.SubjectRepository
import com.example.scheduleui.databinding.FragmentAddNotificationBinding
import com.example.scheduleui.ui.DayScheduleViewModel
import com.example.scheduleui.ui.DayScheduleViewModelFactory
import com.example.scheduleui.util.findNavControllerSafely
import com.example.scheduleui.util.format
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AddNotificationFragment : Fragment() {

    // Arguments of AddNotificationFragment in nav_graph
    private val args: AddNotificationFragmentArgs by navArgs()

    // Binding to fragment_add_notification
    private lateinit var binding: FragmentAddNotificationBinding

    private val viewModel: DayScheduleViewModel by activityViewModels {
        DayScheduleViewModelFactory(
            SubjectRepository((activity?.application as ScheduleApplication).database.scheduleDao()),
            NotifRepository((activity?.application as ScheduleApplication).database.scheduleDao()),
            SettingPreferences(requireContext())
        )
    }

    private var notif: Notification? = null

    private var datetime = LocalDateTime.now().plusHours(1)
        set(value) {
            if (!value.isAfter(LocalDateTime.now())) {
                Toast.makeText(
                    requireContext(), "Đã qua thời gian được chọn...", Toast.LENGTH_SHORT
                ).show()
            } else {
                field = value
                binding.day.text = field.toLocalDate().format(pattern = "ccc, dd/MM/yyyy")
                binding.time.text = field.toLocalTime().format(pattern = "HH:mm")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNotificationBinding.inflate(
            LayoutInflater.from(requireContext())
        )

        // Setup toolbar
        setupToolbar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.notificationId != -1) {
            viewModel.getNotifById(args.notificationId).observe(this.viewLifecycleOwner) {
                notif = it
                bind()
            }
        } else {
            setBtnDate()
            setBtnTime()
        }

    }

    private fun bind() {
        notif?.let {
            binding.txtName.setText(it.name)
            binding.txtContent.setText(it.notes)
            datetime = it.time
            binding.loopOption.isChecked = it.isLoop

            setBtnDate()
            setBtnTime()
        }
    }

    private fun setBtnTime() {
        binding.time.text = datetime.toLocalTime().format(pattern = "HH:mm")
        binding.time.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { timePicker, _, _ ->
                    datetime = LocalDateTime.of(
                        datetime.toLocalDate(),
                        LocalTime.of(timePicker.hour, timePicker.minute)
                    )
                },
                datetime.hour,
                datetime.minute,
                true
            )
            timePickerDialog.create()
            timePickerDialog.show()
        }
    }

    private fun setBtnDate() {
        binding.day.text = datetime.toLocalDate().format(pattern = "ccc, dd/MM/yyyy")
        binding.day.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(), { datePicker, _, _, _ ->
                    datetime = LocalDateTime.of(
                        LocalDate.of(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth),
                        datetime.toLocalTime()
                    )
                }, datetime.year, datetime.monthValue - 1, datetime.dayOfMonth
            )
            datePickerDialog.create()
            datePickerDialog.show()
        }
    }

    /**
     * This function is used to set up menu, menuItem, menuItemSelected for AddNotificationFragment
     */
    private fun setupToolbar() {
        // Get toolbar from activity
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar) ?: return

        // Clear old menu
        toolbar.menu.clear()
        // Inflate new menu layout
        toolbar.inflateMenu(R.menu.add_fragment_menu)
        // Set onClick for menu item
        toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.saveMenuItem) {
                if (args.notificationId != -1) {
                    update()
                } else {
                    create()
                }

                true
            } else {
                false
            }
        }
    }

    private fun update() {
        val title = binding.txtName.text?.toString()?.trim()
        val content = binding.txtContent.text.toString().trim()
        val isLoop = binding.loopOption.isChecked

        if (title.isNullOrEmpty()) {
            binding.txtName.error = "Không được để trống!!!"
            return
        } else {
            binding.txtName.error = null
        }

        if (!datetime.isAfter(LocalDateTime.now())) {
            Toast.makeText(
                requireContext(), "Đã qua thời gian được chọn...", Toast.LENGTH_SHORT
            ).show()
            return
        }

        notif?.let {
            viewModel.update(
                it.copy(name = title, notes = content, time = datetime, isLoop = isLoop)
            )
            findNavControllerSafely()?.navigateUp()
        }
    }

    private fun create() {
        val title = binding.txtName.text?.toString()?.trim()
        val content = binding.txtContent.text.toString().trim()
        val isLoop = binding.loopOption.isChecked

        if (title.isNullOrEmpty()) {
            binding.txtName.error = "Không được để trống!!!"
            return
        } else {
            binding.txtName.error = null
        }

        if (!datetime.isAfter(LocalDateTime.now())) {
            Toast.makeText(
                requireContext(), "Đã qua thời gian được chọn...", Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewModel.create(title, content, datetime, isLoop)
        findNavControllerSafely()?.navigateUp()
    }
}