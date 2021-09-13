package com.technoidentity.vitalz.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.technoidentity.vitalz.data.datamodel.notification.NotificationResponseItem
import com.technoidentity.vitalz.databinding.RecyclerViewNotificationsListBinding

class NotificationAdapter() : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<NotificationResponseItem>() {
        override fun areItemsTheSame(
            oldItem: NotificationResponseItem,
            newItem: NotificationResponseItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NotificationResponseItem,
            newItem: NotificationResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtil)
    var notificationItem: List<NotificationResponseItem>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            RecyclerViewNotificationsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return notificationItem.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.binding.apply {
            val notification = notificationItem[position]
            tvNotificationDate.text = notification.createdTime
            tvNotificationTitle.text = notification.description
        }
    }

    inner class NotificationViewHolder(val binding: RecyclerViewNotificationsListBinding) :
        RecyclerView.ViewHolder(binding.root)
}
