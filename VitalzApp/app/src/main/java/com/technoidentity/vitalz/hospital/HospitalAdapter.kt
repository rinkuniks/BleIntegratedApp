package com.technoidentity.vitalz.hospital

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.technoidentity.vitalz.R
import com.technoidentity.vitalz.data.datamodel.hospital_list.HospitalListDataItem
import com.technoidentity.vitalz.databinding.RecyclerViewHospitalListBinding

class HospitalAdapter(val listener: HospitalListFragment) :
    RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<HospitalListDataItem>() {
        override fun areItemsTheSame(
            oldItem: HospitalListDataItem,
            newItem: HospitalListDataItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: HospitalListDataItem,
            newItem: HospitalListDataItem
        ): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffCallBack)
    var hospitals: List<HospitalListDataItem>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
        return HospitalViewHolder(
            RecyclerViewHospitalListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }

    override fun getItemCount() = hospitals.size

    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
        holder.binding.apply {
            val hospital = hospitals[position]
            val fullAddress: String =
                (hospital.address?.let { it.street } + hospital.address?.let { it.city } +
                        hospital.address?.let { it.state } + hospital.address?.let { it.zipCode })
            if (hospital.status == true) {
                tvHospitalName.setTextColor(listener.resources.getColor(R.color.button_blue))
                tvHospitalName.text = hospital.hospitalName
                hospitalContainer.isClickable = true
            } else {
                hospitalContainer.isClickable = false
                tvHospitalName.setTextColor(Color.GRAY)
                tvHospitalName.text = hospital.hospitalName
                tvHospitalId.setTextColor(Color.GRAY)
                tvHospitalId.text = hospital.id
                tvHospitalAddress.setTextColor(Color.GRAY)
                tvHospitalAddress.text = fullAddress
                ivLocation.setColorFilter(Color.GRAY)
            }
        }
    }

    inner class HospitalViewHolder(val binding: RecyclerViewHospitalListBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (hospitals[layoutPosition].status == true) {
                listener.onItemClicked(layoutPosition)
            }
        }
    }
}
