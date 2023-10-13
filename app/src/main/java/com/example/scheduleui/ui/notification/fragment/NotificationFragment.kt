package com.example.scheduleui.ui.notification.fragment

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import java.util.Calendar

private const val NOTIFICATION_EXTRA_NAME = "notification"

class NotificationFragment : Fragment() {
    // Binding to fragment_notification
    private lateinit var binding: FragmentNotificationBinding

    // Alarm manager to set up time notify notifications
    private lateinit var alarmManager: AlarmManager

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

        // Init alarm manager
        alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Setup toolbar
        setupToolbar()
        // Load recycler view
        loadRecycleView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load data for notification list
        loadContent()

        setupNotification()
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
                    loadRecycleView()
                    loadContent()
                    true
                }

                else -> false
            }
        }
    }

    /**
     * This function is used to load recycler view of list notifications
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

    /**
     * This function is used to content of list notifications
     */
    private fun loadContent() {
        viewModel.notifications.observe(this.viewLifecycleOwner) {
            notificationAdapter?.submitList(it)
        }
    }

    /**
     * This function is used to setup notifications and time to notify them
     */
    private fun setupNotification() {

        viewModel.notifications.observe(this.viewLifecycleOwner) { notifications ->
            notifications.forEach { notification ->
                // Create intent to NotificationReceiver
                val intent = Intent(
                    requireContext(), NotificationReceiver::class.java
                )
                // Put notification information
                intent.putExtra(NOTIFICATION_EXTRA_NAME, String.format("%d @ %s @ %s", notification.id, notification.name, notification.notes))
                // Create pending intent
                val pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    notification.id,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                // Set time to notify a notification
                if(notification.loopOption) {
                    // Loop every day
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notification.time.timeInMillis, 24 * 3_600_000, pendingIntent)
                } else {
                    // Don't loop
                    if(notification.time < Calendar.getInstance()) {
                        // If this notification was notified, don't set time to notify it
                        pendingIntent.cancel()
                    } else {
                        // Set time to notify
                        alarmManager.set(AlarmManager.RTC_WAKEUP, notification.time.timeInMillis, pendingIntent)
                    }

                }
            }
        }
    }

}

/**
 * This receiver is used to receiving an intent broadcast and call to service to create notification
 * and notify them
 */
class NotificationReceiver : BroadcastReceiver() {

    // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.run {
            val intent = Intent(p0, NotificationService::class.java)
            // Convert notification with Intent.putExtra()
            intent.putExtra(NOTIFICATION_EXTRA_NAME, p1?.getStringExtra(NOTIFICATION_EXTRA_NAME))
            // Start a notification service by calling Context.startService(Intent)
            (p0 as Context).startService(intent)
        }
    }
}

/**
 * This service is used to create and show notifications
 */
class NotificationService : Service() {
    companion object {
        // Notification channel id
        private const val CHANNEL_ID = "user_notification"

        // Notification channel name
        private const val CHANNEL_NAME = "User Notification"
    }

    // Notification channel
    private lateinit var channel: NotificationChannel

    // Notification manager
    private lateinit var notificationManager: NotificationManager
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    // This function is called by the system every time a client explicitly starts the service
    // by calling Context.startService(Intent)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // Init a notification channel
        channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "User-configured notifications"
        }

        // Register the channel with the system.
        notificationManager =
            application?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Create a notification channel
        notificationManager.createNotificationChannel(channel)

        // Create and show notification
        // Get notification string from intent
        val notification = intent?.getStringExtra(NOTIFICATION_EXTRA_NAME)

        if(notification.isNullOrEmpty()) {
            //Todo case notification is error

            // If notification is null or empty, don't show notification
            Log.d("TKB", "onStartCommand: don't have notification information")
        } else {
            // Get notification information from notification string
            val tmp = notification.split(" @ ")
            // Show notification
            showNotification(tmp[0].toInt(), tmp[1], tmp[2])
        }

        return START_NOT_STICKY
    }

    /**
     * This function is used to create and show a notification
     *
     * @param notificationId
     * @param notificationTitle
     * @param notificationContent
     */
    private fun showNotification(
        notificationId: Int,
        notificationTitle: String,
        notificationContent: String
    ) {
        // Setup layout and content of notification with NotificationCompat.Builder
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // Check permission
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Show notification
            NotificationManagerCompat.from(this)
                .notify(notificationId, builder.build())
        }
    }
}

