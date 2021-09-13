package com.technoidentity.vitalz.bluetooth.connection

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.Channel

@ViewModelScoped
class BleScanner(
    private val bluetoothLeScanner: BluetoothLeScanner,
    private val scanFilters: List<ScanFilter>? = null,
    private val scanSettings: ScanSettings = ScanSettings.Builder().build()
) {

    val channel = Channel<BluetoothDevice>()

    fun startScan() {
        bluetoothLeScanner.startScan(scanChannelCallback)
    }

    fun stopScan() {
        bluetoothLeScanner.stopScan(scanChannelCallback)
    }

    private val scanChannelCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            result.device?.let { bleDevice: BluetoothDevice ->
                channel.offer(bleDevice)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            results.forEach { result: ScanResult ->
                result.device?.let { bleDevice: BluetoothDevice ->
                    channel.offer(bleDevice)
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            channel.close(ScanFailedException(errorCode))
        }
    }
}