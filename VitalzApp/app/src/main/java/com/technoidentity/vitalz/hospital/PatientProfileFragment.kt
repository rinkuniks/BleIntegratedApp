package com.technoidentity.vitalz.hospital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.technoidentity.vitalz.data.datamodel.single_patient.SinglePatientDashboardResponse
import com.technoidentity.vitalz.databinding.FragmentViewProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientProfileFragment : Fragment() {

    private lateinit var binding: FragmentViewProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewProfileBinding.inflate(layoutInflater)

        //Getting Arguments From last Fragment
        val profileData: SinglePatientDashboardResponse? = arguments?.getParcelable("patientData")
        profileData?.let { setDataInUI(it) }

        binding.ivBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun setDataInUI(profileData: SinglePatientDashboardResponse) {
        binding.apply {
            etName.text = profileData.name
            etAge.text = profileData.age.toString()
            etGender.text = profileData.gender
            etHeight.text = profileData.height.toString()
            etWeight.text = profileData.weight.weight.toString()
            etAddress.text = profileData.address
            tvContactNumber.text = profileData.doctorContactNumber
            etEmergencyContactName.text = profileData.emergencyContactName
            etEmergencyContactNumber.text = profileData.emergencyContactNumber
            etAttendingDoctor.text = profileData.doctorName
            etIdHospital.text = profileData.hospitalId
        }
    }
}