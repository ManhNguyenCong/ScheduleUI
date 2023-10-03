package com.example.scheduleui.ui.notification.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.scheduleui.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {


    private lateinit var binding: FragmentNotificationBinding

//    private val viewModel: ScheduleViewModel by activityViewModels {
//        ScheduleViewModelFactory()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        setupToolbar()

        // Load notification list
        loadRecycleView()
    }

    /**
     * This function is used to setup menu, menuItemSelected for NotificationFragment
     */
    private fun setupToolbar() {
        // Get toolbar from activity
//        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar) ?: return
//
//        // Clean old menu
//        toolbar.menu.clear()
//        // Inflate menu layout
//        toolbar.inflateMenu(R.menu.detail_fragment_menu)
//        // Repair menu item
//        toolbar.menu.removeItem(R.id.addRemindMenuItem)
//        toolbar.menu.removeItem(R.id.goToMenuItem)
//        toolbar.menu.findItem(R.id.comeBackMenuItem).icon =
//            ResourcesCompat.getDrawable(resources, R.drawable.baseline_add_24, null)
//        // Set onClick for menu item
//        toolbar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.comeBackMenuItem -> {
//                    //Todo Xử lý sự kiện thêm thông báo
//                    val action =
//                        NotificationFragmentDirections.actionNotificationFragmentToAddNotificationFragment()
//                    findNavControllerSafely()?.navigate(action)
//                    Toast.makeText(context, "Thêm thông báo", Toast.LENGTH_SHORT).show()
//                    true
//                }
//
//                R.id.refreshMenuItem -> {
//                    //Todo xử lý sự kiện làm mới
//                    Toast.makeText(context, "Refresh", Toast.LENGTH_SHORT).show()
//                    loadRecycleView()
//                    true
//                }
//
//                else -> false
//            }
//        }
    }

    /**
     * This function is used to load data of notification list
     */
    private fun loadRecycleView() {
        //todo đổ dữ liệu vào recycleview
//        val adapter = NotificationAdapter { view, notificationId ->
//
//            val popupMenu = PopupMenu(context, view)
//
//            popupMenu.menu.add("Sửa")
//            popupMenu.menu.add("Xóa")
//
//            popupMenu.setOnMenuItemClickListener { menuItem ->
//                when (menuItem.title) {
//                    "Sửa" -> {
//                        val action =
//                            NotificationFragmentDirections.actionNotificationFragmentToAddNotificationFragment(
//                                notificationId
//                            )
//                        findNavControllerSafely()?.navigate(action)
//                        true
//                    }
//
//                    "Xóa" -> {
//                        Toast.makeText(
//                            context,
//                            "Delete notification $notificationId",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        true
//                    }
//
//                    else -> false
//                }
//            }
//
//            popupMenu.show()
//        }
//        adapter.submitList(viewModel.notifications)
//        binding.notificationList.adapter = adapter
//        binding.notificationList.layoutManager = LinearLayoutManager(this.context)
    }
}