package com.groomers.groomersvendor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.groomers.groomersvendor.databinding.ServiceItemBinding
import com.groomers.groomersvendor.model.modelservice.Result

class ServiceAdapter(private val serviceList: List<Result>) : RecyclerView.Adapter<ServiceAdapter.ServiceViewMode>() {
    inner class ServiceViewMode(val binding : ServiceItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewMode {
        val binding = ServiceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ServiceViewMode(binding)
    }

    override fun getItemCount(): Int = serviceList.size

    override fun onBindViewHolder(holder: ServiceViewMode, position: Int) {
        with(serviceList[position]){
            holder.binding.serviceName.text = serviceName
            holder.binding.slotTime.text = slot_time
            holder.binding.serviceType.text = serviceType
            holder.binding.tvDescription.text = description
        }

    }
}