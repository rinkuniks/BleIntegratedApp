package com.technoidentity.vitalz.hospital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.technoidentity.vitalz.databinding.FragmentPatientProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientUpdateProfileFragment : Fragment() {
    private lateinit var binding: FragmentPatientProfileBinding
    val viewModel: PatientUpdateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientProfileBinding.inflate(layoutInflater)

        val patientName: String = binding.etName.text.toString()
        val age: String = binding.etAge.text.toString()
        val gender: String = binding.etGender.text.toString()
        val height: String = binding.etHeight.text.toString()
        val weight: String = binding.etWeight.text.toString()
        val address: String = binding.etAddress.text.toString()
        val phoneNumber: String = binding.etContactNumber.text.toString()
        val emergencyContactName: String = binding.etEmergencyContactName.text.toString()
        val emergencyContactNumber: String = binding.etEmergencyContactNumber.text.toString()
        val attendingDoctor: String = binding.etAttendingDoctor.text.toString()
        val hospitalId: String = binding.etIdHospital.text.toString()

        binding.btnSubmit.setOnClickListener {
            //validate All fields Are filled
            validateFields(
                patientName,
                age,
                gender,
                height,
                weight,
                address,
                phoneNumber,
                emergencyContactName,
                emergencyContactNumber,
                attendingDoctor,
                hospitalId
            )
        }

        return binding.root
    }

    private fun validateFields(
        patientName: String,
        age: String,
        gender: String,
        height: String,
        weight: String,
        address: String,
        phoneNumber: String,
        emergencyContactName: String,
        emergencyContactNumber: String,
        attendingDoctor: String,
        hospitalId: String
    ) {
        when {
            patientName.isEmpty() -> {
                Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show()
            }
            age.isEmpty() -> {
                Toast.makeText(context, "Please enter age", Toast.LENGTH_SHORT).show()
            }
            gender.isEmpty() -> {
                Toast.makeText(context, "Please enter gender", Toast.LENGTH_SHORT).show()
            }
            height.isEmpty() -> {
                Toast.makeText(context, "Please enter height", Toast.LENGTH_SHORT).show()
            }
            weight.isEmpty() -> {
                Toast.makeText(context, "Please enter weight", Toast.LENGTH_SHORT).show()
            }
            address.isEmpty() -> {
                Toast.makeText(context, "Please enter address", Toast.LENGTH_SHORT).show()
            }
            phoneNumber.isEmpty() -> {
                Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT).show()
            }
            emergencyContactName.isEmpty() -> {
                Toast.makeText(context, "Please enter emergency contact name", Toast.LENGTH_SHORT)
                    .show()
            }
            emergencyContactNumber.isEmpty() -> {
                Toast.makeText(context, "Please enter emergency contact number", Toast.LENGTH_SHORT)
                    .show()
            }
            attendingDoctor.isEmpty() -> {
                Toast.makeText(context, "Please enter attending doctor", Toast.LENGTH_SHORT).show()
            }
            hospitalId.isEmpty() -> {
                Toast.makeText(context, "Please enter hospitalId", Toast.LENGTH_SHORT).show()
            }
            else -> {
                viewModel.updatePatient( patientName,
                    age,
                    gender,
                    height,
                    weight,
                    address,
                    phoneNumber,
                    emergencyContactName,
                    emergencyContactNumber,
                    attendingDoctor,
                    hospitalId,
                "get from pref // VITHOS007" ).observe(viewLifecycleOwner,{
                })
            }
        }
    }

}