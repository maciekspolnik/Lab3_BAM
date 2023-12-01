package ms.projects.lab3

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ms.projects.lab3.databinding.ActivityMainBinding
import ms.projects.lab3.receiver.BluetoothStateReceiver
import ms.projects.lab3.receiver.NetworkStateReceiver
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    companion object {
        const val CONTACTS_TAG = "Contacts"
        const val API_RESULT_TAG = "API_RESULT"
        const val READ_CONTACTS_PERMISSION_REQUEST_CODE = 12345
        const val SMS_PERMISSION_REQUEST_CODE = 12344

        val url = URL("https://jsonplaceholder.typicode.com/posts")
    }

    private val networkStateReceiver = NetworkStateReceiver()
    private val bluetoothStateReceiver = BluetoothStateReceiver()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener { callApi() }

        binding.button2.setOnClickListener {
            askForPermissions(
                Manifest.permission.READ_CONTACTS,
                READ_CONTACTS_PERMISSION_REQUEST_CODE
            )
        }
        askForPermissions(
            Manifest.permission.RECEIVE_SMS,
            SMS_PERMISSION_REQUEST_CODE
        )
        registerReceiver(networkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        registerReceiver(bluetoothStateReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_CONTACTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts()
                return
            }
            Toast.makeText(this, "PERMISSION NOT GRANTED", Toast.LENGTH_SHORT).show()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun callApi() {
        try {
            GlobalScope.launch {
                with(withContext(Dispatchers.IO) {
                    url.openConnection()
                } as HttpURLConnection) {
                    inputStream.bufferedReader().use {
                        it.lines().forEach { line ->
                            Log.d(API_RESULT_TAG, line)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("ERROR", e.toString())
        }

    }

    private fun readContacts() {
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (cursor!!.moveToNext()) {
            val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val displayName =
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            Log.d(CONTACTS_TAG, "Contact $contactId $displayName")
        }
        cursor.close()

    }

    private fun askForPermissions(permission: String, code: Int) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission),
            code
        )
    }

    override fun onDestroy() {
        unregisterReceiver(networkStateReceiver)
        unregisterReceiver(bluetoothStateReceiver)
        super.onDestroy()
    }

}