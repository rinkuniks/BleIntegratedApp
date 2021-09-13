package com.technoidentity.vitalz.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.utils.isTablet
import com.technoidentity.vitalz.utils.showToast

class HomeFragment : Fragment() {

    val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()

        if (isTablet(requireContext())) {
            sharedViewModel.isDeviceConnected.observe(this@HomeFragment) {
                showToast(requireContext(), "homefragment - isdeviceconnected $it")
                // Navigating based on ble device connection
                if (!it) {
                    findNavController().navigate(R.id.action_homeFragment_to_doctorNurseLoginFragment)
                } else {
                    findNavController().navigate(R.id.action_homeFragment_to_singlePatientDashboardFragment)
                }
            }
        } else {
            findNavController().navigate(R.id.action_homeFragment_to_userSelectionFragment2)
        }
    }
}