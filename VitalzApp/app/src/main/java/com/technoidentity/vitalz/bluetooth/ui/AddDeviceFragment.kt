package com.technoidentity.vitalz.bluetooth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.base.BaseFragment
import com.technoidentity.vitalz.databinding.FragmentAddDeviceBinding
import com.technoidentity.vitalz.home.SharedViewModel


class AddDeviceFragment : BaseFragment<FragmentAddDeviceBinding>() {

    override fun getViewBinding() = FragmentAddDeviceBinding.inflate(layoutInflater)

    val viewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addDeviceBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addDeviceFragment_to_bleScanResultFragment)
        }

    }
}
