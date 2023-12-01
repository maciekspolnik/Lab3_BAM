package ms.projects.lab3.receiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BluetoothStateReceiver : BroadcastReceiver() {
    companion object {
        const val BLUETOOTH_TAG = "BluetoothStateReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter() as
                BluetoothAdapter
        bluetoothAdapter.let {

            Log.d(BLUETOOTH_TAG, "BLUETOOTH ENABLED: ${bluetoothAdapter.isEnabled}")
        }
    }
}