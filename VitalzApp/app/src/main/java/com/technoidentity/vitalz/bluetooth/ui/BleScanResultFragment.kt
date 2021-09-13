package com.technoidentity.vitalz.bluetooth.ui

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.bluetooth.connection.BleConnection
import com.technoidentity.vitalz.bluetooth.data.BleMac
import com.technoidentity.vitalz.data.network.Constants
import com.technoidentity.vitalz.databinding.FragmentBlescanResultBinding
import com.technoidentity.vitalz.home.SharedViewModel
import com.technoidentity.vitalz.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import timber.log.Timber

@AndroidEntryPoint
class BleScanResultFragment : Fragment() {

    val viewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FragmentBlescanResultBinding

    private val devices = ArrayList<BluetoothDevice>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlescanResultBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val deviceListAdapter =
            BluetoothScanResultAdapter(BleDeviceClickListener { bluetoothDevice ->
                viewModel.toggleScan().also {
                    showToast(requireContext(), "Scanning stopped")
                }
                viewModel.connectDevice(bluetoothDevice, requireContext().applicationContext)

            }, viewModel)

        viewModel.run {
            connectedDeviceData.observe(viewLifecycleOwner) {
                if (it.connectionStatus == BleConnection.DeviceConnected) {

                    it.gatt?.getService(DEVICE_BATTERY_SER_UUID)?.let { deviceBatteryService ->

                        readCharacteristics(it.device, DEVICE_BATTERY_CHAR_UUID, deviceBatteryService)
                        //Enable notification to recieve changes from device battery status
                        //enableNotifications(it.device, deviceBatteryService.getCharacteristic(DEVICE_BATTERY_CHAR_UUID))

                    }
                    // navigate after device is connected
                    findNavController().navigate(R.id.action_bleScanResultFragment_to_deviceDetailsFragment) // will take bledevice object from viewmodel
                }
            }
        }
//                viewModel.deviceBattery.observe(viewLifecycleOwner) {
//                    if (it > 0) {
        //showToast(requireContext(), "Device is connected BleScanresult")
        //findNavController().navigate(R.id.action_bleScanResultFragment_to_deviceDetailsFragment) // will take bledevice object from viewmodel
//                    } else showToast(requireContext(), "Device is not connected BleScanresult")
//                }

        scanResult(deviceListAdapter)
    }


    private fun scanResult(deviceListAdapter: BluetoothScanResultAdapter) {
        binding.bleScanResult.apply {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = deviceListAdapter
            layoutManager = LinearLayoutManager(context)
        }
        deviceListAdapter.submitList(devices)

        binding.scanBtn.setOnClickListener {
            viewModel.toggleScan()
        }

        binding.scanBtn.apply {
            viewModel.isScanning.observe(viewLifecycleOwner, {
                if (it) setText(R.string.stop_scan) else setText(R.string.start_scan)
            })
        }

        lifecycleScope.launchWhenCreated {

            viewModel.scanFlow.distinctUntilChangedBy { it.address }.collect { device ->
                val indexQuery = devices.indexOfFirst { it.address == device.address }

                when (device.name != null && device.name.startsWith("HRM_")) {

                    true -> {
                        viewModel.deviceForRegisteration(BleMac(device.address))
                            viewModel.registeredDevice?.let {
                                if (it.patchId != "Invalid_Patch") {
                                    if (indexQuery != -1) {
                                        devices[indexQuery] = device
                                    } else {
                                        devices.add(device)
                                        deviceListAdapter.notifyItemChanged(devices.size - 1)
                                        Timber.d("found device with address = ${device.address}")
                                    }
                                }

                            }

                    }
                    false -> {
                        if (indexQuery != -1) {
                            devices[indexQuery] = device
                        } else {
                            devices.add(device)
                            deviceListAdapter.notifyItemChanged(devices.size - 1)
                            Timber.d("found device with address = ${device.address}")
                        }
                    }
                }


            }
        }

    }


}
