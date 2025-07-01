package com.groomers.groomersvendor.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ehcf.Helper.currency
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.activity.OrderDetail
import com.groomers.groomersvendor.databinding.ServiceItemBinding
import com.groomers.groomersvendor.model.modelservice.Result

class ServiceAdapter(val context: Context,private val serviceList: List<Result>,private val deleteService: DeleteService) : RecyclerView.Adapter<ServiceAdapter.ServiceViewMode>() {
    inner class ServiceViewMode(val binding : ServiceItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewMode {
        val binding = ServiceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ServiceViewMode(binding)
    }

    override fun getItemCount(): Int = serviceList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ServiceViewMode, position: Int) {
        with(serviceList[position]){
            holder.binding.tvServiceName.text = serviceName
            holder.binding.slotTime.text = date
            holder.binding.tvServiceType.text = serviceType
            holder.binding.tvDescription.text = description
            holder.binding.tvPrice.text = currency +price.toString()
            holder.binding.tvUserType.text = user_type
            holder.binding.btnDelete.setOnClickListener {
                deleteService.deleteService(id.toString())
            }
            Glide.with(context)
                .load("https://groomers.co.in/public/uploads/" + image)
                .into(holder.binding.imageView)

            holder.binding.btnEdit.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("openFragment", "AddPostFragment")
                intent.putExtra("id", id.toString())
                context.startActivity(intent)
            }

            holder.binding.imageView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, OrderDetail::class.java)
                 intent.putExtra("id", id.toString())
                context.startActivity(intent)
            }
        }

    }
}

interface DeleteService{
    fun deleteService(id : String)
}