package com.technoidentity.vitalz.user

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.base.BaseFragment
import com.technoidentity.vitalz.data.network.Constants
import com.technoidentity.vitalz.databinding.FragmentDocnurseLoginBinding
import com.technoidentity.vitalz.home.SharedViewModel
import com.technoidentity.vitalz.utils.Constants.DOCTOR
import com.technoidentity.vitalz.utils.Constants.NURSE
import com.technoidentity.vitalz.utils.CustomProgressDialog
import com.technoidentity.vitalz.utils.isTablet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorNurseLoginFragment : BaseFragment<FragmentDocnurseLoginBinding>() {

    override fun getViewBinding() = FragmentDocnurseLoginBinding.inflate(layoutInflater)

    val viewModel: DoctorNurseLoginViewModel by viewModels()
    val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var progressDialog: CustomProgressDialog
    private var isPasswordVisible: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = CustomProgressDialog(this.requireContext())

        binding.btnLoginDocNurse.setOnClickListener {
            validateCredentials(
                binding.etUserName.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        //Password visibility
        binding.etPassword.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.ivPasswordVisibility.setOnClickListener {
            if (!isPasswordVisible) {
                isPasswordVisible = true
                binding.ivPasswordVisibility.setImageResource(R.drawable.ic_baseline_remove_red_eye_24)
                binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                isPasswordVisible = false
                binding.etPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.ivPasswordVisibility.setImageResource(R.drawable.ic_baseline_visibility_off_24)
            }
        }
    }

    private fun validateCredentials(username: String, password: String) {
        when {
            username.isEmpty() -> {
                Toast.makeText(context, "Please Enter username", Toast.LENGTH_SHORT).show()
            }
            password.isEmpty() -> {
                Toast.makeText(context, "Please Enter Password", Toast.LENGTH_SHORT).show()
            }
            else -> {
                progressDialog.showLoadingDialog()
                viewModel.sendDocNurseCredentials(username,password).observe(viewLifecycleOwner, {
                    if (it.token != null){
                        val pref =
                            context?.getSharedPreferences(Constants.PREFERENCE_NAME, 0)
                        pref?.edit()?.putString(Constants.TOKEN, it.token)?.apply()
                        progressDialog.dismissLoadingDialog()
                        if (it.user?.role == DOCTOR) {
                            sharedViewModel.checkRole(DOCTOR)
                        }else{
                            sharedViewModel.checkRole(NURSE)
                        }
                        //check for tablet or mobile and navigate
                        when (isTablet(requireContext())) {
                            false ->
                            {
                                findNavController().navigate(R.id.action_doctorNurseLoginFragment_to_multiPatientDashboardFragment)
                                val pref =
                                    context?.getSharedPreferences(Constants.PREFERENCE_NAME, 0)
                                pref?.edit()?.putString(Constants.DOCTOR_MOBILE, it.user?.phoneNo)?.apply()
                            }
                            true ->
                            {
                                sharedViewModel.isDeviceConnected.observe(viewLifecycleOwner) { deviceConnected ->
                                    if (!deviceConnected) {
                                        findNavController().navigate(R.id.action_doctorNurseLoginFragment_to_addDeviceFragment)
                                    } else {
                                        findNavController().navigate(R.id.action_doctorNurseLoginFragment_to_singlePatientDashboardFragment)
                                    }
                                }
                            }
                        }
                    } else{
                        progressDialog.dismissLoadingDialog()
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}