package com.technoidentity.vitalz.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.databinding.FragmentUserSelectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSelectionFragment : Fragment() {

    private lateinit var binding: FragmentUserSelectionBinding
    val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSelectionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hospitalLayout.setOnClickListener {
            sharedViewModel.isCareTakerSelected(false)
            findNavController().navigate(R.id.action_userSelectionFragment2_to_doctorNurseLoginFragment)
        }
        binding.patientCareLayout.setOnClickListener {
            sharedViewModel.isCareTakerSelected(true)
            findNavController().navigate(R.id.action_userSelectionFragment2_to_careTakerMobileLoginFragment)
        }
    }
}
