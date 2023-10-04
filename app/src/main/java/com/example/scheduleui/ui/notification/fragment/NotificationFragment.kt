package com.example.scheduleui.ui.notification.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduleui.R
import com.example.scheduleui.data.ScheduleApplication
import com.example.scheduleui.databinding.FragmentNotificationBinding
import com.example.scheduleui.ui.notification.adapter.NotificationAdapter
import com.example.scheduleui.ui.notification.viewmodel.NotificationViewModel
import com.example.scheduleui.ui.notification.viewmodel.NotificationViewModelFactory
import com.example.scheduleui.util.findNavControllerSafely

class NotificationFragment : Fragment() {

    // Binding to fragment_notification
    private lateinit var binding: FragmentNotificationBinding

    // Notification view model
    private val viewModel: NotificationViewModel by activityViewModels {
        NotificationViewModelFactory(
            (activity?.application as ScheduleApplication).database.scheduleDao()
        )
    }

    // Notification adapter
    private var notificationAdapter: NotificationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        // Setup toolbar
        setupToolbar()
        // Load recycler view
        loadRecycleView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load data for notification list
        viewModel.notifications.observe(this.viewLifecycleOwner) {
            notificationAdapter?.submitList(it)
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
                    //Todo xử lý sự kiện làm mới
                    Toast.makeText(context, "Refresh", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }

    /**
     * This function is used to recycler view of list notifications
     */
    private fun loadRecycleView() {
        notificationAdapter = NotificationAdapter { view, notification ->
            // Event click "more vert icon" in each notification to open popup menu

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
                        // Delete this notification
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
        binding.notificationList.layoutManager = LinearLayoutManager(this.context)
    }
}