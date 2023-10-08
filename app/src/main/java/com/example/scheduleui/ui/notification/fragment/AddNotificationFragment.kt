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
import com.example.scheduleui.data.ScheduleApplication
import com.example.scheduleui.databinding.FragmentAddNotificationBinding
import com.example.scheduleui.ui.notification.viewmodel.NotificationViewModel
import com.example.scheduleui.ui.notification.viewmodel.NotificationViewModelFactory
import com.example.scheduleui.util.findNavControllerSafely
import com.example.scheduleui.util.formatDayScheduleDate
import com.example.scheduleui.util.formatDayScheduleTime
import com.example.scheduleui.util.getDateFromString
import com.example.scheduleui.util.getTimeFromTimeString
import java.util.Calendar

class AddNotificationFragment : Fragment() {

    // Arguments of AddNotificationFragment in nav_graph
    private val args: AddNotificationFragmentArgs by navArgs()

    // Binding to fragment_add_notification
    private lateinit var binding: FragmentAddNotificationBinding

    // Notification view model
    private val viewModel: NotificationViewModel by activityViewModels {
        NotificationViewModelFactory(
            (activity?.application as ScheduleApplication).database.scheduleDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNotificationBinding.inflate(
            LayoutInflater.from(requireContext())
        )

        // Setup toolbar
        setupToolbar()
        // Load fragment
        loadFragment()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load content
        loadContent()

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
                if(args.notificationId != -1) {
                    // Update notification
                    updateNotification()
                } else {
                    // Add new a notification
                    addNewNotification()
                }

                true
            } else {
                false
            }
        }
    }

    /**
     * This function is used to load elements in this fragment
     */
    private fun loadFragment() {
        // Set date picker dialog for btnDay
        binding.day.setOnClickListener {
            // Get current day in button text
            val currentDay = binding.day.text.toString().getDateFromString()
            // Init date picker dialog
            val dayPickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    // Set day choose to btnDay
                    binding.day.text =
                        Calendar.Builder().setDate(year, month, dayOfMonth).build()
                            .formatDayScheduleDate()
                },
                currentDay.get(Calendar.YEAR),
                currentDay.get(Calendar.MONTH),
                currentDay.get(Calendar.DAY_OF_MONTH)
            )
            // Show date picker dialog
            dayPickerDialog.show()
        }

        // Set time picker dialog for btnTime
        binding.time.setOnClickListener {
            // Get current time from btnTime
            val currentTime = binding.time.text.toString().getTimeFromTimeString()
            // Init time picker dialog
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    binding.time.text =
                        Calendar.Builder().setTimeOfDay(hour, minute, 0).build()
                            .formatDayScheduleTime()
                },
                currentTime.get(Calendar.HOUR_OF_DAY),
                currentTime.get(Calendar.MINUTE),
                true
            )
            // Show time picker dialog
            timePickerDialog.show()
        }
    }

    /**
     * This function is used to load content of this fragment
     */
    private fun loadContent() {
        if(args.notificationId != -1) { // Edit fragment
            viewModel.getNotificationById(args.notificationId).observe(this.viewLifecycleOwner) { notification ->
                // Set txtName, txtContent
                binding.txtName.setText(notification.name)
                binding.txtContent.setText(notification.notes)
                // Set btnTime, btnDay
                binding.time.text = notification.time.formatDayScheduleTime()
                binding.day.text = notification.time.formatDayScheduleDate()
            }
        } else { // Add fragment
            // Set day, time is current time
            binding.day.text = Calendar.getInstance().formatDayScheduleDate()
            binding.time.text = Calendar.getInstance().formatDayScheduleTime()
        }
    }

    /**
     * This function is used to validate notification entry (name isn't empty)
     */
    private fun validNotificationEntry(): Boolean {
        return viewModel.validNotificationEntry(binding.txtName.text.toString())
    }

    /**
     * This function is used to add new a notification
     */
    private fun addNewNotification() {
        if(validNotificationEntry()) {
            // Add new a notification
            viewModel.addNewNotification(
                binding.txtName.text.toString(),
                binding.txtContent.text.toString(),
                binding.day.text.toString(),
                binding.time.text.toString(),
                binding.loopOption.text.toString()
            )
            // Navigate to NotificationFragment
            val action =
                AddNotificationFragmentDirections.actionAddNotificationFragmentToNotificationFragment()
            findNavControllerSafely()?.navigate(action)
        } else {
            Toast.makeText(context, "Enter notification's name ...", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * This function is used to update a notification
     */
    private fun updateNotification() {
        if(validNotificationEntry()) {
            viewModel.updateNotification(
                args.notificationId,
                binding.txtName.text.toString(),
                binding.txtContent.text.toString(),
                binding.day.text.toString(),
                binding.time.text.toString(),
                binding.loopOption.text.toString()
            )
            // Navigate to NotificationFragment
            val action =
                AddNotificationFragmentDirections.actionAddNotificationFragmentToNotificationFragment()
            findNavControllerSafely()?.navigate(action)
        } else {
            Toast.makeText(context, "Enter notification's name ...", Toast.LENGTH_SHORT).show()
        }
    }
}