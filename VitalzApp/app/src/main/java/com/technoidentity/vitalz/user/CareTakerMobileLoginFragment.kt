package com.technoidentity.vitalz.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.data.network.Constants
import com.technoidentity.vitalz.databinding.FragmentCaretakerLoginBinding
import com.technoidentity.vitalz.utils.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CareTakerMobileLoginFragment : Fragment() {

    private lateinit var binding: FragmentCaretakerLoginBinding
    val viewModel : CareTakerMobileViewModel by viewModels()
    private lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCaretakerLoginBinding.inflate(layoutInflater)
        progressDialog = CustomProgressDialog(this.requireContext())

        binding.btnRequestOtp.setOnClickListener {
            val mobileNumber = binding.etMobileNumber.text.toString()
            validateMobileNumber(mobileNumber)
        }
        return binding.root
    }

    private fun validateMobileNumber(mobile: String) {
        when {
            mobile.isEmpty() -> {
                binding.responseMsg.visibility = View.VISIBLE
                binding.responseMsg.setText(R.string.empty)
            }
            mobile.length != 10 -> {
                binding.responseMsg.visibility = View.VISIBLE
                binding.responseMsg.setText(R.string.shortLength)
            }
            !mobile.matches(Constants.mobilePattern.toRegex()) -> {
                binding.responseMsg.visibility = View.VISIBLE
                binding.responseMsg.setText(R.string.invalid)
            }
            mobile.matches(Constants.mobilePattern.toRegex()) -> {
                //do api call request and on success response navigate to next EnterOTP Fragment
                binding.responseMsg.visibility = View.GONE
                binding.responseMsg.text = ""
                progressDialog.showLoadingDialog()
                    viewModel.getCareTakerResponse(mobile).observe(viewLifecycleOwner, {
                        if (it.success == true){
                            progressDialog.dismissLoadingDialog()
                            val bundle = bundleOf("mobileNumber" to mobile)
                            findNavController().navigate(
                                R.id.action_careTakerMobileLoginFragment_to_careTakerMobileOTPFragment, bundle)
                        }else{
                            Toast.makeText(context, it.reason, Toast.LENGTH_SHORT).show()
                            progressDialog.dismissLoadingDialog()
                        }
                    })
            }
            else -> {
                binding.responseMsg.visibility = View.VISIBLE
                binding.responseMsg.setText(R.string.unknown)
            }
        }
    }
}
