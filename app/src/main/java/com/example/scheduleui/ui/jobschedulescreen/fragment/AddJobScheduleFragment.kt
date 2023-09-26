package com.example.scheduleui.ui.jobschedulescreen.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.scheduleui.R
import com.example.scheduleui.databinding.FragmentAddJobScheduleBinding
import com.example.scheduleui.findNavControllerSafely

class AddJobScheduleFragment : Fragment() {

    private val args: AddJobScheduleFragmentArgs by navArgs()

    private lateinit var binding: FragmentAddJobScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddJobScheduleBinding.inflate(
            LayoutInflater.from(context)
        )

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
     * This function is used to setup menu, menuItemSelected for AddJoBScheduleFragment
     *
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
                    AddJobScheduleFragmentDirections.actionAddJobScheduleFragmentToJobScheduleListFragment()
                findNavControllerSafely()?.navigate(action)
                //todo save jobSchedule

                true
            } else false
        }
    }

    /**
     * This function is used to load edit fragment or add fragment
     *
     */
    private fun loadFragment() {
        if (args.jobScheduleId == -1) {
            //todo load add fragment
            binding.btnDayStart.text = "Thứ..., 01/01/1970"
            binding.btnDayEnd.text = "Thứ..., 01/01/1970"
            binding.btnTimeStart.text = "00:00"
            binding.btnTimeEnd.text = "00:00"

        } else {
            //todo load edit fragment
        }

        // If full day, ignore choose time
        binding.isFullDay.setOnClickListener {
            if (binding.isFullDay.isChecked) {
                binding.btnTimeStart.visibility = View.GONE
                binding.btnTimeEnd.visibility = View.GONE
            } else {
                binding.btnTimeStart.visibility = View.VISIBLE
                binding.btnTimeEnd.visibility = View.VISIBLE
            }
        }

        // Set onclick button datePicker and timePicker
        binding.btnDayStart.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    binding.btnDayStart.text =
                        String.format("Thứ ..., %02d/%02d/%d", dayOfMonth, month, year)
                    //todo process choose day start of jobSchedule
                },
                1970,
                1,
                1
            )
            datePickerDialog.show()
        }
        binding.btnDayEnd.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    binding.btnDayEnd.text =
                        String.format("Thứ ..., %02d/%02d/%d", dayOfMonth, month, year)
                    //todo process choose day end of jobSchedule
                },
                1970,
                1,
                1
            )
            datePickerDialog.show()
        }
        binding.btnTimeStart.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    binding.btnTimeStart.text = String.format("%02d:%02d", hourOfDay, minute)
                    //todo process choose time start of jobSchedule
                },
                0,
                0,
                true
            )
            timePickerDialog.show()
        }
        binding.btnTimeEnd.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    binding.btnTimeEnd.text = String.format("%02d:%02d", hourOfDay, minute)
                    //todo process choose time start of jobSchedule
                },
                0,
                0,
                true
            )
            timePickerDialog.show()
        }

        // Setup context menu
        registerForContextMenu(binding.loopOption)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu.add(getString(R.string.loopOption0))
        menu.add(getString(R.string.loopOption1))
        menu.add(getString(R.string.loopOption2))
        menu.add(getString(R.string.loopOption3))
        menu.add(getString(R.string.loopOption4))
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.title) {
            getString(R.string.loopOption0) -> {
                binding.loopOption.text = getString(R.string.loopOption0)
                true
            }
            getString(R.string.loopOption1) -> {
                binding.loopOption.text = getString(R.string.loopOption1)
                true
            }
            getString(R.string.loopOption2) -> {
                binding.loopOption.text = getString(R.string.loopOption2)
                true
            }
            getString(R.string.loopOption3) -> {
                binding.loopOption.text = getString(R.string.loopOption3)
                true
            }
            getString(R.string.loopOption4) -> {
                binding.loopOption.text = getString(R.string.loopOption4)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}