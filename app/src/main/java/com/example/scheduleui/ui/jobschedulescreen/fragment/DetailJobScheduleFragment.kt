package com.example.scheduleui.ui.jobschedulescreen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.scheduleui.databinding.FragmentDetailJobScheduleBinding

class DetailJobScheduleFragment : Fragment() {

    private val args: DetailJobScheduleFragmentArgs by navArgs()

    private lateinit var binding: FragmentDetailJobScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailJobScheduleBinding.inflate(
            LayoutInflater.from(context)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        // Setup toolbar
//        setupToolbar()
//
//        //Load fragment
//        loadFragment()
    }

//    /**
//     * This function is used to setup menu, menuItemSelected for DetailJobSchedule
//     */
//    private fun setupToolbar() {
//        // Get toolbar from activity
//        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar) ?: return
//
//        // Clean old menu
//        toolbar.menu.clear()
//        // Inflate new menu layout
//        toolbar.inflateMenu(R.menu.detail_fragment_menu)
//        // Repair menu item
//        toolbar.menu.findItem(R.id.editObjectMenuItem).title = "Sửa"
//        toolbar.menu.findItem(R.id.editObjectMenuItem)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT)
//        // Set onClick for menu item
//        toolbar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.editObjectMenuItem -> {
//                    // Navigate to AddJobScheduleFragment
//                    val action =
//                        DetailJobScheduleFragmentDirections.actionDetailJobScheduleFragmentToAddJobScheduleFragment(
//                            jobScheduleId = args.jobScheduleId
//                        )
//                    findNavControllerSafely()?.navigate(action)
//                    true
//                }
//
//                R.id.deleteObjectMenuItem -> {
//                    val action =
//                        DetailJobScheduleFragmentDirections.actionDetailJobScheduleFragmentToJobScheduleListFragment()
//                    findNavControllerSafely()?.navigate(action)
//                    Toast.makeText(context, "Delete job " + args.jobScheduleId, Toast.LENGTH_SHORT)
//                        .show()
//                    true
//                }
//
//                else -> false
//            }
//        }
//    }
//
//    /**
//     * This function is used to bind data for elements in fragment
//     */
//    private fun loadFragment() {
//        binding.name.text = "Job A"
//        binding.time.text = "10:10 - 11:10\nThứ 6, 06/10/2023"
//        binding.location.text = "Location A"
//        binding.notes.text = "Notes..."
//    }

}