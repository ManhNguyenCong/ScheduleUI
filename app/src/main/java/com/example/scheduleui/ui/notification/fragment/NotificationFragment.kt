package com.example.scheduleui.ui.notification.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.scheduleui.R
import com.example.scheduleui.ScheduleApplication
import com.example.scheduleui.data.localdatabase.SettingPreferences
import com.example.scheduleui.data.model.Notification
import com.example.scheduleui.data.repository.NotifRepository
import com.example.scheduleui.data.repository.SubjectRepository
import com.example.scheduleui.databinding.FragmentNotificationBinding
import com.example.scheduleui.ui.DayScheduleViewModel
import com.example.scheduleui.ui.DayScheduleViewModelFactory
import com.example.scheduleui.ui.notification.adapter.NotificationAdapter
import com.example.scheduleui.util.findNavControllerSafely

class NotificationFragment : Fragment() {
    // Binding to fragment_notification
    private lateinit var binding: FragmentNotificationBinding

    // Notification adapter
    private lateinit var notificationAdapter: NotificationAdapter

    private val viewModel: DayScheduleViewModel by activityViewModels {
        DayScheduleViewModelFactory(
            SubjectRepository((activity?.application as ScheduleApplication).database.scheduleDao()),
            NotifRepository((activity?.application as ScheduleApplication).database.scheduleDao()),
            SettingPreferences(requireContext())
        )
    }

    private var notifs: List<Notification>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        // Setup toolbar
        setupToolbar()

        notificationAdapter = NotificationAdapter(requireContext()) { view, notification ->
            // Init popup menu
            val popupMenu = PopupMenu(context, view)
            // Add menu to popup menu
            popupMenu.menu.add(resources.getString(R.string.editMenuItemTitle))
            popupMenu.menu.add(resources.getString(R.string.deleteMenuItemTitle))
            // Set up menu item onClick
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.title) {
                    resources.getString(R.string.editMenuItemTitle) -> {
                        // Navigate to add notification fragment
                        val action =
                            NotificationFragmentDirections.actionNotificationFragmentToAddNotificationFragment(
                                notification.id
                            )
                        findNavControllerSafely()?.navigate(action)
                        true
                    }

                    resources.getString(R.string.deleteMenuItemTitle) -> {
                        viewModel.delete(notification)
                        true
                    }

                    else -> false
                }
            }
            // Show popup menu
            popupMenu.show()
        }
        binding.notificationList.adapter = notificationAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNotifications().observe(this.viewLifecycleOwner) {
            notifs = it
            notificationAdapter.submitList(notifs)
        }
    }

    /**
     * This function is used to setup menu, menuItemSelected for NotificationFragment
     */
    private fun setupToolbar() {
        // Get toolbar from activity
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar) ?: return

        // Clean old menu
        toolbar.menu.clear()
        // Inflate menu layout
        toolbar.inflateMenu(R.menu.list_fragment_menu)
        // Repair menu item
        toolbar.menu.removeItem(R.id.addRemindMenuItem)
        toolbar.menu.removeItem(R.id.goToMenuItem)
        toolbar.menu.findItem(R.id.comeBackMenuItem).icon =
            ResourcesCompat.getDrawable(resources, R.drawable.baseline_add_24, null)
        // Set onClick for menu item
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.comeBackMenuItem -> {
                    // Navigate to add notification fragment
                    val action =
                        NotificationFragmentDirections.actionNotificationFragmentToAddNotificationFragment()
                    findNavControllerSafely()?.navigate(action)
                    true
                }

                R.id.refreshMenuItem -> {
                    notificationAdapter.submitList(null)
                    notificationAdapter.submitList(notifs)
                    true
                }

                else -> false
            }
        }
    }
}