package ms.projects.lab3.receiver

import android.provider.Telephony.Sms.Intents.getMessagesFromIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SmsReceiver : BroadcastReceiver() {
    companion object {
        const val SMS_TAG = "SmsReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        getMessagesFromIntent(intent).forEach {
            Log.d(SMS_TAG, "Message from: ${it.originatingAddress}")
            Log.d(SMS_TAG, "Content: ${it.messageBody}")
        }
    }
}
