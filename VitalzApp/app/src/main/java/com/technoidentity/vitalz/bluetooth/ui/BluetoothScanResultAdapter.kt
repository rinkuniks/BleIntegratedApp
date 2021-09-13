package com.technoidentity.vitalz.bluetooth.ui

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.databinding.DeviceItemListBinding
import com.technoidentity.vitalz.home.SharedViewModel

class BluetoothScanResultAdapter(private val bleDeviceClickListener: BleDeviceClickListener, val viewModel: SharedViewModel) :
    ListAdapter<BluetoothDevice, BluetoothScanResultAdapter.DeviceListViewHolder>(DeviceDiffCallBack()) {

    lateinit var binding: DeviceItemListBinding

     //var sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceListViewHolder {

        binding = DeviceItemListBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.device_item_list, parent, false))
        return DeviceListViewHolder(binding.root)
    }

    override fun onBindViewHolder(viewHolder: DeviceListViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.bindView(item, bleDeviceClickListener)

    }

    inner class DeviceListViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bindView(bleDevice: BluetoothDevice, bleDeviceClickListener: BleDeviceClickListener) {
            viewModel.registeredDevice(bleDevice).apply {
                if(this.first){
                    binding.bleDeviceName.text = this.second // patch id
                    binding.bleMacTxt.visibility = View.GONE
                } else {
                    binding.bleDeviceName.text = this.second
                    binding.bleMacTxt.text = this.third
                }
                binding.root.setOnClickListener { bleDeviceClickListener.onClick(bleDevice) }
            }

        }
    }

}

class DeviceDiffCallBack: DiffUtil.ItemCallback<BluetoothDevice>() {
    override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
        return oldItem.address == newItem.address
    }
}
class BleDeviceClickListener(val onClickListener: (bleDevice: BluetoothDevice) -> Unit) {
    fun onClick(bleDevice: BluetoothDevice)  = onClickListener(bleDevice)
}