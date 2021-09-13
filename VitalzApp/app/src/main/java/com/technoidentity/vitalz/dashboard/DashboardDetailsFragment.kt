package com.technoidentity.vitalz.dashboard

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.data.datamodel.dashboardDetail.DashboardDetailResponse
import com.technoidentity.vitalz.data.network.Constants
import com.technoidentity.vitalz.databinding.FragmentDashboardDetailBinding
import com.technoidentity.vitalz.home.SharedViewModel
import com.technoidentity.vitalz.utils.CustomProgressDialog
import com.technoidentity.vitalz.utils.VitalzConstant.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class DashboardDetailsFragment : Fragment() {

    lateinit var binding: FragmentDashboardDetailBinding
    private lateinit var progressDialog: CustomProgressDialog
    val viewModel: DashboardDetailsViewModel by viewModels()
    val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var patientId: String
    lateinit var itemSelected: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardDetailBinding.inflate(layoutInflater)
        progressDialog = CustomProgressDialog(this.requireContext())

        //get Shared data
        val sharedPreferences =
            context?.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        patientId = sharedPreferences?.getString(Constants.PATIENTID, null).toString()

        binding.ivBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        //Calender
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy")

        //End Date
        binding.layoutEndDateSelect.isClickable = false
        //set today's date in End Date
        val endDate = dateFormat.format(cal.time)
        binding.tvSelectedEndDate.text = endDate
        Log.v("EndDate", "$endDate")

        //Start Date
        //Subtracting 7 days from selected End date
        cal.add(Calendar.DAY_OF_MONTH, -6)
        val startDate = dateFormat.format(cal.time)
        binding.tvSelectedStartDate.text = startDate
        Log.v("StartDate", "$startDate")

        binding.layoutStartDateSelect.setOnClickListener {
            //Always check for month because they start from 0 as jan and 11 as dec.
            // So, add 1 in month. And Add 2 Days more than present day & change the color of the DatePicker
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                R.style.DialogTheme,
                DatePickerDialog.OnDateSetListener { view, mDay, mMonth, mYear ->
                    val date = "" + mDay + "-" + (mMonth + 1) + "-" + mYear
                    binding.tvSelectedStartDate.text = date

                }, day, month, year
            )
            //Setting Max. Date from Selected End date
            datePickerDialog.datePicker.maxDate = cal.timeInMillis
            datePickerDialog.datePicker.minDate = cal.timeInMillis - 6
            datePickerDialog.show()
        }

        //Getting Arguments From last Fragment
//        - heartrate
//        - respiratory
//        - bloodpressure
//        - temprature
//        - oxygen
        //Getting Arguments From last Fragment
        //Getting Arguments From last Fragment
        when (arguments?.getString("isAlive")) {
            HEART_RATE.item -> {
                binding.tvHeartRate.text = "Heart Rate"
                itemSelected = HEART_RATE.item
                setInitialDates(itemSelected)
            }
            RESPIRATORY.item -> {
                binding.tvHeartRate.text = "Respiratory"
                itemSelected = RESPIRATORY.item
                setInitialDates(itemSelected)
            }
            BLOOD_PRESSURE.item -> {
                binding.tvHeartRate.text = "Blood Pressure"
                itemSelected = BLOOD_PRESSURE.item
                setInitialDates(itemSelected)
            }
            TEMPERATURE.item -> {
                binding.tvHeartRate.text = "Temperature"
                itemSelected = TEMPERATURE.item
                setInitialDates(itemSelected)
            }
            OXYGEN.item -> {
                binding.tvHeartRate.text = "Oxygen"
                itemSelected = OXYGEN.item
                setInitialDates(itemSelected)
            }
        }

        //Bar Chart
        initializeBarChart()
        setBarChart()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setInitialDates(item: String) {

                    //Default dates Api-Call
//        getSelectedDaysData(defaultStartDate, defaultEndDate, item, it)

    }

    private fun getSelectedDaysData(
        startDate: String,
        endDate: String,
        item: String,
        patientId: String
    ) {
        progressDialog.showLoadingDialog()
        viewModel.getDashboardDetailsData(patientId, item, startDate, endDate)
            .observe(viewLifecycleOwner, {
                if (it != null) {
                    progressDialog.dismissLoadingDialog()
                    setDetailsData(it)
                } else {
                    progressDialog.dismissLoadingDialog()
                    Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setDetailsData(it: DashboardDetailResponse) {
        binding.tvAverageRate.text = it.weeklyAverage.toString()
        binding.tvMaxRate.text = it.weeklyMax.toString()
        binding.tvMinRate.text = it.weeklyMin.toString()
        //set values in graph list -> it.itemData
    }

    private fun setBarChart() {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(100f, 10f))
        entries.add(BarEntry(200f, 20f))
        entries.add(BarEntry(300f, 30f))
        entries.add(BarEntry(400f, 40f))
        entries.add(BarEntry(500f, 50f))
        entries.add(BarEntry(600f, 60f))

//        for (element in heartData.ratePerMinute)
//            entries.addAll(listOf(BarEntry(element.toFloat(), 0f)))

        val barDataSet = BarDataSet(entries, "Cells")

        val data = BarData(barDataSet)
        binding.heartBarChart.data = data // set the data and list of labels into chart

        data.barWidth = 30f
        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.color = resources.getColor(R.color.button_blue)

        binding.heartBarChart.animateY(5000)
    }

    private fun initializeBarChart() {

        binding.heartBarChart.description.isEnabled = false
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        binding.heartBarChart.setMaxVisibleValueCount(5)
        binding.heartBarChart.setVisibleXRange(1F, 5F)
        binding.heartBarChart.xAxis.setDrawGridLines(true)
        // scaling can now only be done on x- and y-axis separately
        binding.heartBarChart.setPinchZoom(false)
        binding.heartBarChart.setDrawBarShadow(true)
        binding.heartBarChart.setDrawGridBackground(false)
        val xAxis: XAxis = binding.heartBarChart.xAxis
        xAxis.setDrawGridLines(true)
        binding.heartBarChart.axisLeft.setDrawGridLines(false)
        binding.heartBarChart.axisRight.setDrawGridLines(false)
        binding.heartBarChart.axisRight.isEnabled = false
        binding.heartBarChart.axisLeft.isEnabled = true
        binding.heartBarChart.xAxis.setDrawGridLines(false)
        // add a nice and smooth animation
        binding.heartBarChart.animateY(1500)
        binding.heartBarChart.legend.isEnabled = true
        binding.heartBarChart.axisRight.setDrawLabels(false)
        binding.heartBarChart.axisLeft.setDrawLabels(true)
        binding.heartBarChart.setTouchEnabled(false)
        binding.heartBarChart.isDoubleTapToZoomEnabled = false
        binding.heartBarChart.xAxis.isEnabled = true
        binding.heartBarChart.xAxis.position = XAxisPosition.BOTTOM
        binding.heartBarChart.invalidate()
    }
}
