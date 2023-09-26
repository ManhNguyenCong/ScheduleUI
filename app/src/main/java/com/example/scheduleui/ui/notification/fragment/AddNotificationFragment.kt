package com.example.scheduleui.ui.notification.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.scheduleui.R
import com.example.scheduleui.databinding.FragmentAddNotificationBinding
import com.example.scheduleui.findNavControllerSafely

class AddNotificationFragment : Fragment() {

    private val args: AddNotificationFragmentArgs by navArgs()

    private lateinit var binding: FragmentAddNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNotificationBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        setupToolbar()

        // load fragment
        loadFragment()
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
        toolbar.inflateMenu(R.menu.add_day_schedule_menu)
        // Set onClick for menu item
        toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.saveMenuItem) {
                val action =
                    AddNotificationFragmentDirections.actionAddNotificationFragmentToNotificationFragment()
                findNavControllerSafely()?.navigate(action)
                //todo save new notification
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

        if (args.notificationId == -1) {
            //Todo load fragment add notification
            binding.day.text = "THỨ ..., 01/01/1970"
            binding.time.text = "00:00"
        } else {
            //Todo load fragment edit notification
            binding.txtName.setText("Thông báo A")
            binding.txtContent.setText("Nội dung")
            binding.day.text = "THỨ ..., 01/01/1970"
            binding.time.text = "00:00"
            //Kiểm tra lặp
            binding.loopOption.isChecked = true
        }

        //Todo setup button day and time
        binding.day.setOnClickListener {
            val dayPickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    binding.day.text =
                        String.format("THỨ ..., %02s/%02s/$%s", dayOfMonth, month, year)
                },
                1970,
                1,
                1
            )
            dayPickerDialog.show()
        }

        binding.time.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    binding.time.text = String.format("%02d:%02d", hour, minute)
                },
                0,
                0,
                true
            )

            timePickerDialog.show()
        }
    }
}