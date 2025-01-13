# My First Practical app
Safety check in app
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import java.util.Calendar
import android.app.admin.DevicePolicyManager
import android.content.Context.DEVICE_POLICY_SERVICE
import kotlinx.coroutines.*


class SafetyCheckInApp(private val activity: Activity) {

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private val REQUEST_CODE_DEVICE_ADMIN = 123

    fun startCheckIn(context: Context, intervalMillis: Long) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, CheckInReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.MILLISECOND, intervalMillis.toInt())
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            intervalMillis,
            pendingIntent
        )
    }

    fun stopCheckIn() {
        alarmManager.cancel(pendingIntent)
    }

    fun requestDeviceAdmin() {
        val devicePolicyManager = activity.getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(activity, AdminReceiver::class.java)

        if (!devicePolicyManager.isAdminActive(componentName)) {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "This app requires device administrator privileges to perform safety check-ins and automatically contact authorities in case of an emergency.")
            activity.startActivityForResult(intent, REQUEST_CODE_DEVICE_ADMIN)
        }
    }
}

class CheckInReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the alarm triggers
        // 1. Check if the user has checked in recently
        // 2. If not, trigger the emergency protocol:
        //    - Send SMS to emergency contacts
        //    - Initiate a phone call to emergency number
        //    - Play the pre-recorded audio message

        // (Implementation for steps 1 and 2 will go here)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Replace with actual emergency contact numbers
                sendSMS(context, "+15551234567", "Emergency! I need help!") 
                sendSMS(context, "+15559876543", "Emergency! I need help!")

                // Replace with actual emergency number
                makeEmergencyCall(context, "+15555555555") 
            } catch (e: Exception) {
                // Handle exceptions (e.g., log the error)
            }
        }
    }

    // (Helper functions for sending SMS and making phone calls will go here)
    private suspend fun sendSMS(context: Context, phoneNumber: String, message: String) {
        // Use SmsManager to send the SMS
    }

    private suspend fun makeEmergencyCall(context: Context, phoneNumber: String) {
        // Use intent to initiate a phone call
    }
}
