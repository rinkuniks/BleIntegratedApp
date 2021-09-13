package com.technoidentity.vitalz.dashboard

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.bluetooth.connection.BleConnection
import com.technoidentity.vitalz.data.datamodel.single_patient.SinglePatientDashboardResponse
import com.technoidentity.vitalz.data.datamodel.updatedregistereddevice.UpdateRegisteredDeviceRequest
import com.technoidentity.vitalz.data.datamodel.updatedregistereddevice.UpdatedRegisteredDeviceResponse
import com.technoidentity.vitalz.data.network.Constants
import com.technoidentity.vitalz.databinding.FragmentSinglePaitentDashboardBinding
import com.technoidentity.vitalz.home.SharedViewModel
import com.technoidentity.vitalz.notifications.datamodel.VitalzTelemetryNotification
import com.technoidentity.vitalz.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import kotlin.properties.Delegates

@AndroidEntryPoint
class SinglePatientDashboardFragment : Fragment() {

    private val singlePatientDashboardViewModel: SinglePatientDashboardViewModel by viewModels()
    val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var binding: FragmentSinglePaitentDashboardBinding
    private lateinit var progressDialog: CustomProgressDialog
    lateinit var singlePatientDashboardResponse: SinglePatientDashboardResponse
    lateinit var updatedRegisteredDeviceResponse: UpdatedRegisteredDeviceResponse
    lateinit var patientId: String
    lateinit var patchId: String
    lateinit var deviceConnectionStatus: BleConnection
    var batteryValue by Delegates.notNull<String>()
    var heartRateGraphData = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSinglePaitentDashboardBinding.inflate(layoutInflater)
        progressDialog = CustomProgressDialog(this.requireContext())

        //check if nurse or caretaker
        //nurse -> visibility of BLE layout
        if (!isTablet(requireContext())) {
            binding.layoutBle.visibility = View.VISIBLE
        } else {
            binding.layoutBle.visibility = View.GONE
        }

        patchId = arguments?.getString("patchId").toString()
        //Get Patient Id from Shared Prefs
        val sharedPreferences =
            context?.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        patientId = sharedPreferences?.getString(Constants.PATIENTID, null).toString()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutHeartRate.setOnClickListener {
            findNavController().navigate(
                R.id.action_singlePatientDashboardFragment_to_singlePatientDetailFragment,
                bundleOf("isAlive" to VitalzConstant.HEART_RATE.item)
            )
        }

        binding.layoutRespiratory.setOnClickListener {
            findNavController().navigate(
                R.id.action_singlePatientDashboardFragment_to_singlePatientDetailFragment,
                bundleOf("isAlive" to "respiratory")
            )
        }
    }

    override fun onResume() {
        super.onResume()

        when (isTablet(requireContext())) {
            true -> {
                bluetoothData(patchId)
                binding.ivViewProfile.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_singlePatientDashboardFragment_to_patientProfileFragment,
                        bundleOf("patientData" to singlePatientDashboardResponse)
                    )
                }

            }
            false -> {
                singleDashboardApi(patientId)
                binding.ivViewProfile.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_singlePatientDashboardFragment_to_patientProfileFragment,
                        bundleOf("patientData" to singlePatientDashboardResponse)
                    )
                }

            }
        }

        lifecycleScope.launchWhenResumed {

            heartRateBle(patchId)
            bodyPostureDataBle()
            bodyTemperatureDataBle()
        }

    }

    private fun setPieChartData(heartEntries: List<Entry>) {
        //Heart
        with(binding.pieChartHeartLayout) {
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            xAxis.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = false
        }

        val dataSetHeart = LineDataSet(heartEntries, "Unused label").apply {
            color = Color.RED
            setDrawCircles(false)
            valueTextColor = Color.GRAY
            highLightColor = Color.RED
            setDrawValues(false)
            lineWidth = 3f
            isHighlightEnabled = true
            setDrawHighlightIndicators(false)
        }

        binding.pieChartHeartLayout.data = LineData(dataSetHeart)
        binding.pieChartHeartLayout.invalidate()

        //Respiratory
        with(binding.pieChartRespiratoryLayout) {
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            xAxis.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = false
        }

//        val respiratoryEntries: List<Entry> =
//            singlePatientDashboardResponse.respiratoryRate.ratePerMinute.mapIndexed { index, i ->
//                Entry(
//                    index.toFloat(),
//                    i.toFloat()
//                )
//            }
//        val dataSetRespiratory = LineDataSet(respiratoryEntries, "Unused label").apply {
//            color = Color.BLUE
//            setDrawCircles(false)
//            valueTextColor = Color.GRAY
//            highLightColor = Color.RED
//            setDrawValues(false)
//            lineWidth = 3f
//            isHighlightEnabled = true
//            setDrawHighlightIndicators(false)
//        }
//
//        binding.pieChartRespiratoryLayout.data = LineData(dataSetRespiratory)
//        binding.pieChartRespiratoryLayout.invalidate()
    }

    private fun bluetoothData(patchId: String) {
        //progressDialog.showLoadingDialog()

        sharedViewModel.connectedDeviceData.observe(viewLifecycleOwner, {
            deviceConnectionStatus = it.connectionStatus

            if (it.connectionStatus == BleConnection.DeviceConnected) {
                it.gatt?.getService(HEART_RATE_SER_UUID)?.let { heartRateService ->
                    sharedViewModel.readCharacteristics(
                        it.device,
                        HEART_RATE_CHAR_UUID,
                        heartRateService
                    )
                }
                sharedViewModel.deviceBattery.observe(viewLifecycleOwner,{
                    binding.tvTimeLeft.text = it.toString().plus("%")
                    batteryValue = it.toString()
                })
            }
        })

        updateDeviceStatus()

        }

  private fun updateDeviceStatus() {

      if(deviceConnectionStatus == BleConnection.DeviceConnected) {
          // replace static patchid with argument patchid from this method
          singlePatientDashboardViewModel.updateDevice(
              UpdateRegisteredDeviceRequest(
                  batteryValue,
                  "VITHOS001",
                  ACTIVE_STATUS,
                  CONNECTION_GOOD
              )
          )
          singlePatientDashboardViewModel.updateDevice.observe(viewLifecycleOwner) { registeredDeviceResponse ->
              // updating patientId to dashboarddetailsfragment
              //updatePatientId(registeredDeviceResponse.patientId)
              singleDashboardApi(registeredDeviceResponse.patientId)
              //heartRateBle()
              updatedRegisteredDeviceResponse = registeredDeviceResponse
              binding.tvSelectedPatient.text = registeredDeviceResponse.patientName
          }
      } else {
          singlePatientDashboardViewModel
              .updateDevice(
                  UpdateRegisteredDeviceRequest(
                      batteryValue,
                      patchId,
                      INACTIVE_STATUS,
                      CONNECTION_BAD
                  )
              )
          singlePatientDashboardViewModel.updateDevice.observe(viewLifecycleOwner) { registeredDeviceResponse ->
              updatedRegisteredDeviceResponse = registeredDeviceResponse
          }
      }

  }



    private suspend fun heartRateBle(patchId: String) {

        sharedViewModel.heartRateData.collect {

            it.forEach { heartRate ->

                //for line graph
                heartRateGraphData.add(heartRate.toInt())

                if (this::singlePatientDashboardResponse.isInitialized) {
                    if (checkHeartRateThreshold( Pair(singlePatientDashboardResponse.age, heartRate))) {

                        singlePatientDashboardViewModel.sendTelemetryNotification(
                            VitalzTelemetryNotification(
                                patchId,
                                HEART_RATE_DATA,
                                heartRate.toString()
                            )
                        ).apply {

                            if (this == true) {
                                showToast(requireContext(), "HeartRate Notification sent")
                            }
                        }
                    }

                    binding.tvHeartRateCount.text = heartRate.toString().also {
                        Timber.d("heartrate $it")
                    }

                    setGraphData(heartRateGraphData)
                }

            }
            if(!it.contains(0)) {
                sharedViewModel.sendHeartRateToServer(patchId, HEART_RATE_DATA, it.asList())
            }
        }
    }


    /**
     *  Use this for settings graph data
     */
    private fun setGraphData(graphData: MutableList<Int>) {

        graphData.mapIndexed { index, i ->
            Entry(index.toFloat(), i.toFloat())
        }.run {
            setPieChartData(this)
        }
    }


    private suspend fun bodyPostureDataBle() {
        sharedViewModel.bodyPosture.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.tvPostureValue.text = it.also {
                    Timber.i("body ${it}")
                }
            } else {
                binding.tvPostureValue.text = "".also {
                    Timber.i("body ${it}")
                }
            }
        }
    }

    private suspend fun bodyTemperatureDataBle() {
        sharedViewModel.bodyTemperature.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.tvTemperatureValue.text = it.also {
                    Timber.i("temp ${it}")
                }
            } else {
                binding.tvTemperatureValue.text = "".also {
                    Timber.i("temp ${it}")
                }
            }
        }
    }


    private fun singleDashboardApi(patientId: String) {
        //progressDialog.showLoadingDialog()
        singlePatientDashboardViewModel.getSinglePatientData(patientId)
        singlePatientDashboardViewModel.singlePatientData.observe(viewLifecycleOwner, {
            when (it) {
                is SinglePatientDashboardViewModel.SinglePatient.Success -> {
                    //progressDialog.dismissLoadingDialog()
                    it.data.apply {
                        singlePatientDashboardResponse = this
                        setData(this)
                        this.heartRate.ratePerMinute.mapIndexed { index, i ->
                            Entry(index.toFloat(), i.toFloat())
                        }.run {
                            setPieChartData(this)
                        }
                    }

                }

                is SinglePatientDashboardViewModel.SinglePatient.Failure -> {
                    progressDialog.dismissLoadingDialog()
                    Toast.makeText(context, it.errorText, Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        })
    }

    private fun setData(data: SinglePatientDashboardResponse) {

        binding.apply {
            tvSelectedPatient.text = data.name
            if (isTablet(requireContext())) {
                tvHeartRateCount.text = data.heartRate.ratePerMinute.last().toString()
                tvTimeLeft.text = data.device.batteryPercentage.toString()
            }
            tvRespiratoryCount.text = data.respiratoryRate.ratePerMinute.last().toString()
            tvTemperatureValue.text = data.temperature.bodyTemperature.last().toString()
            tvBpValue.text = (data.bloodPressure.systolicPressure.last()
                .toString() + "/" + data.bloodPressure.diastolicPressure.last().toString())
            tvActivityValue.text = data.step.stepCount.toString()
            tvPostureValue.text = data.posture.bodyPosture.last()
            tvOxygenSaturationValue.text =
                data.oxygenSaturation.oxygenPercentage.last().toString()
            tvWeightValue.text = data.weight.weight.toString()

        }
    }

}

