package com.technoidentity.vitalz.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.technoidentity.vitalz.data.datamodel.multiple_patient.MultiplePatientDashboardResponseItem
import com.technoidentity.vitalz.databinding.RecyclerViewDocPatientListBinding

class MultiplePatientAdapter(val listener: MultiPatientDashboardFragment) :
    RecyclerView.Adapter<MultiplePatientAdapter.MultiplePatientViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<MultiplePatientDashboardResponseItem>() {
        override fun areItemsTheSame(
            oldItem: MultiplePatientDashboardResponseItem,
            newItem: MultiplePatientDashboardResponseItem
        ): Boolean {
            return oldItem.patientId == newItem.patientId
        }

        override fun areContentsTheSame(
            oldItem: MultiplePatientDashboardResponseItem,
            newItem: MultiplePatientDashboardResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffCallBack)
    var multiplePatient: List<MultiplePatientDashboardResponseItem>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiplePatientViewHolder {
        return MultiplePatientViewHolder(
            RecyclerViewDocPatientListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }

    override fun getItemCount() = multiplePatient.size

    override fun onBindViewHolder(holder: MultiplePatientViewHolder, position: Int) {
        holder.binding.apply {
            val patientsInfo = multiplePatient[position]
            tvPatientName.text = patientsInfo.name
            tvBloopPressureCount.text = (patientsInfo.bloodPressure.systolicPressure.last().toString() +
                    "/" + patientsInfo.bloodPressure.diastolicPressure.last().toString())
            tvTempCount.text = patientsInfo.temperature.bodyTemperature.last().toString()
            tvSpoCount.text = patientsInfo.oxygenSaturation.oxygenPercentage.last().toString()
            tvPosCount.text = patientsInfo.posture.bodyPosture.last()
        }
    }

    inner class MultiplePatientViewHolder(val binding: RecyclerViewDocPatientListBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClicked(layoutPosition)
        }
    }
}
