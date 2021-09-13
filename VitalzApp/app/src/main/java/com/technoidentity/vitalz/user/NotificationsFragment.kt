package com.technoidentity.vitalz.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.technoidentity.vitalz.dashboard.MultiPatientDashboardViewModel
import com.technoidentity.vitalz.dashboard.MultiplePatientAdapter
import com.technoidentity.vitalz.databinding.FragmentNotificationsBinding
import com.technoidentity.vitalz.home.HomeActivity
import com.technoidentity.vitalz.home.SharedViewModel
import com.technoidentity.vitalz.utils.Constants
import com.technoidentity.vitalz.utils.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    lateinit var binding: FragmentNotificationsBinding
    private lateinit var progressDialog: CustomProgressDialog
    val viewModel: NotificationViewModel by viewModels()
    val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater)
        progressDialog = CustomProgressDialog(this.requireContext())

        //Shared Prefs
        val sharedPreferences =
            context?.getSharedPreferences(
                com.technoidentity.vitalz.data.network.Constants.PREFERENCE_NAME,
                Context.MODE_PRIVATE
            )
        val mobile = sharedPreferences?.getString(
            com.technoidentity.vitalz.data.network.Constants.DOCTOR_MOBILE,
            null
        ).toString()
        val patientId = sharedPreferences?.getString(
            com.technoidentity.vitalz.data.network.Constants.PATIENTID,
            null
        ).toString()

        //setup Adapter
        setUpRecyclerView()

        when (arguments?.getString("assignedRole")) {
            Constants.DOCTOR -> {
                getDoctorNotifications(mobile)
            }
            Constants.NURSE -> {
                getNurseNotifications()
            }
            Constants.CARETAKER -> {
                getCareTakerNotifications(patientId)
            }
        }
        return binding.root
    }

    private fun getCareTakerNotifications(patientId: String) {
        progressDialog.showLoadingDialog()
        viewModel.getNotificationsCareTakerListData(patientId).observe(viewLifecycleOwner) {
            //check for it.reason success case
            if (it.isNotEmpty()) {
                progressDialog.dismissLoadingDialog()
                notificationAdapter.notificationItem = it
                sharedViewModel.notificationCount.value = it.size
            } else {
                progressDialog.dismissLoadingDialog()
            }
        }
    }

    private fun getNurseNotifications() {
        progressDialog.showLoadingDialog()
        viewModel.getNotificationsAdminNurseListData().observe(viewLifecycleOwner) {
            //check for it.reason success case
            if (it.isNotEmpty()) {
                progressDialog.dismissLoadingDialog()
                notificationAdapter.notificationItem = it
                sharedViewModel.notificationCount.value = it.size
            } else {
                progressDialog.dismissLoadingDialog()
            }
        }
    }

    private fun getDoctorNotifications(mobile: String) {
        progressDialog.showLoadingDialog()
        viewModel.getNotificationsDoctorListData(mobile).observe(viewLifecycleOwner) {
            //check for it.reason success case
            if (it.isNotEmpty()) {
                progressDialog.dismissLoadingDialog()
                sharedViewModel.notificationCount.value = it.size
                notificationAdapter.notificationItem = it
            } else {
                progressDialog.dismissLoadingDialog()
            }
        }
    }

    private fun setUpRecyclerView() {
        notificationAdapter = NotificationAdapter()
        binding.rvNotificationList.adapter = notificationAdapter
        binding.rvNotificationList.layoutManager = LinearLayoutManager(context)
    }
}
