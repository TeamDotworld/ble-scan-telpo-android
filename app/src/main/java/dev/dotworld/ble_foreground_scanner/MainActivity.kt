package dev.dotworld.ble_foreground_scanner

import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private var scanning = false

    lateinit var resultView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultView = findViewById(R.id.result)

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        val bluetoothManager =
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val mBluetoothAdapter = bluetoothManager.adapter

        bluetoothLeScanner = mBluetoothAdapter.bluetoothLeScanner

        val filter = ScanFilter.Builder()
            // .setServiceUuid(ParcelUuid(UUID.fromString(serviceUUID)))
            .build()

        val filters: ArrayList<ScanFilter> = ArrayList()
        filters.add(filter)

        val settings = ScanSettings.Builder()
            .setReportDelay(0)
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        bluetoothLeScanner.startScan(filters, settings, BleScanCallback(this))
        resultView.text = "Scanning started..."
        /*if (Build.VERSION.SDK_INT >= LOLLIPOP && !forceLegacy) {
            scanManager = LollipopScanManager(reactContext, this)
        } else {
            scanManager = LegacyScanManager(reactContext, this)
        }*/
    }


    class BleScanCallback(private val context: Context) : ScanCallback() {

        companion object {
            private const val TAG = "BleScanCallback"
        }

        private fun processScanResult(scanResult: ScanResult?) {
            Log.i(TAG, "processScanResult: scanResult : $scanResult")
            scanResult?.let { result ->
                val device = result.device
                val rssi = result.rssi // get RSSI value


                Log.i(
                    TAG,
                    "processScanResult: Device : $device -- rssi : $rssi -- needed - "
                )

            }
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            processScanResult(result)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)

            val reason = when (errorCode) {
                SCAN_FAILED_ALREADY_STARTED -> "$errorCode - SCAN_FAILED_ALREADY_STARTED"
                SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> "$errorCode - SCAN_FAILED_APPLICATION_REGISTRATION_FAILED"
                SCAN_FAILED_FEATURE_UNSUPPORTED -> "$errorCode - SCAN_FAILED_FEATURE_UNSUPPORTED"
                SCAN_FAILED_INTERNAL_ERROR -> "$errorCode - SCAN_FAILED_INTERNAL_ERROR"
                else -> {
                    "$errorCode - UNDOCUMENTED"
                }
            }
            Log.e(TAG, "BT Scan failed: $reason")
        }
    }

}