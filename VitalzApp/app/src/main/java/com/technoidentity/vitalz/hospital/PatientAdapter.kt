package com.technoidentity.vitalz.hospital

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.technoidentity.vitalz.data.datamodel.patient_list.PatientDataListItem
import com.technoidentity.vitalz.databinding.RecyclerViewPatientListBinding

class PatientAdapter(val listener: PatientListFragment) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<PatientDataListItem>() {
        override fun areItemsTheSame(
            oldItem: PatientDataListItem,
            newItem: PatientDataListItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PatientDataListItem,
            newItem: PatientDataListItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtil)
    var patient: List<PatientDataListItem>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        return PatientViewHolder(
            RecyclerViewPatientListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return patient.size
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.binding.apply {
            val patient = patient[position]
            tvPatientName.text = patient.name
            tvPatientAge.text = patient.age.toString()
            tvPatientSex.text = patient.gender
            tvPatientId.text = patient.id.toString()
            tvPatientHospitalAddress.text = patient.address
        }
    }

    inner class PatientViewHolder(val binding: RecyclerViewPatientListBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClicked(layoutPosition)
        }
    }
}
