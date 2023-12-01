package ms.projects.lab3.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log

class NetworkStateReceiver : BroadcastReceiver() {
    companion object {
        const val NETWORK_TAG = "NetworkStateReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(NETWORK_TAG, "Network State Intent Received")

        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager
        connectivityManager.let {
            val networkInfo = it.activeNetworkInfo
            Log.d(NETWORK_TAG, "Is connected: ${networkInfo?.isConnected}")
            Log.d(NETWORK_TAG, "WIFI: ${networkInfo?.type} ${ConnectivityManager.TYPE_WIFI}")
        }
    }
}