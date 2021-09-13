package com.technoidentity.vitalz.hospital

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.data.network.Constants
import com.technoidentity.vitalz.databinding.FragmentPatientListBinding
import com.technoidentity.vitalz.utils.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientListFragment : Fragment(), PatientAdapter.OnItemClickListener {

    lateinit var binding: FragmentPatientListBinding
    lateinit var hospitalId : String
    lateinit var mobile : String
    val viewModel: PatientViewModel by viewModels()
    private lateinit var patientAdapter: PatientAdapter
    private lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientListBinding.inflate(inflater)
        progressDialog = CustomProgressDialog(this.requireContext())

        //Getting Arguments From last Fragment
        mobile = arguments?.getString("mobile").toString()
        hospitalId = arguments?.getString("hospitalId").toString()

        //setup RecyclerView
        setUpRecyclerView()

        //backBtn
        binding.ivBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        //Api call
        getPatientList(mobile, hospitalId)

        //In Search Cancel button visibility GONE , please enable while typing
        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    binding.ivCancelSearch.visibility = View.GONE
                    getPatientList(mobile,hospitalId)
                }else{
                    binding.ivCancelSearch.visibility = View.VISIBLE
                    if (start == 2){
                        searchPatient(s)
                        binding.ivCancelSearch.setOnClickListener {
                            binding.etSearch.setText("")
                            getPatientList(mobile,hospitalId)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        return binding.root
    }

    private fun searchPatient(text: CharSequence) {
        progressDialog.showLoadingDialog()
        viewModel.searchPatientInList(text).observe(viewLifecycleOwner, {
            if (it.isNotEmpty()){
                patientAdapter.patient = it
                progressDialog.dismissLoadingDialog()
            }else{
                progressDialog.dismissLoadingDialog()
                Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getPatientList(mobile: String, hospitalId: String) {
        progressDialog.showLoadingDialog()
        viewModel.getPatientListData(mobile, hospitalId).observe(viewLifecycleOwner,{
            if (it.isNotEmpty()){
                patientAdapter.patient = it
                progressDialog.dismissLoadingDialog()
            }else{
                progressDialog.dismissLoadingDialog()
                Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUpRecyclerView() = binding.rvPatientList.apply {
        patientAdapter = PatientAdapter(this@PatientListFragment)
        adapter = patientAdapter
        layoutManager = LinearLayoutManager(context)
    }

    override fun onItemClicked(position: Int) {
        val pref = context?.getSharedPreferences(Constants.PREFERENCE_NAME, 0)
        pref?.edit()?.putString(Constants.PATIENTID, patientAdapter.patient[position].id.toString())?.apply()
        findNavController().navigate(
            R.id.action_patientListFragment_to_singlePatientDashboardFragment)
    }
}
