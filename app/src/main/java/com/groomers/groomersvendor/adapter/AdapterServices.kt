package com.groomers.groomersvendor.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.About
import com.groomers.groomersvendor.databinding.SingleRowServicesBinding
import com.groomers.groomersvendor.model.modelcategory.Result
import com.groomers.groomersvendor.viewmodel.MyApplication

class AdapterServices(private val categoryList: List<Result>, val context: Context) : RecyclerView.Adapter<AdapterServices.CategoryViewHolder>() {

    private var selectedPosition = -1 // No item selected initially

    inner class CategoryViewHolder(val binding: SingleRowServicesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = SingleRowServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(categoryList[position]) {
            holder.binding.tvName.text = category_name
            Glide.with(context)
                .load("https://groomers.co.in/public/uploads/" + category_image)
                .into(holder.binding.iamgeView)

            // Set background color based on selected position
            // Set background color based on selected position
            if (selectedPosition == position) {
                holder.binding.llHaircut.setBackgroundResource(R.drawable.selected_card)
            } else {
                holder.binding.llHaircut.setBackgroundResource(R.drawable.card_background)
            }

            holder.binding.llHaircut.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
                serviceName=categoryList[position].category_name
            }
        }
    }
    companion object{
        var serviceName=""
    }
}
