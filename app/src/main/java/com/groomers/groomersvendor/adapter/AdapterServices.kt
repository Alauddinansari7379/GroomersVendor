package com.groomers.groomersvendor.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.SingleRowServicesBinding
import com.groomers.groomersvendor.model.modelcategory.Result
class AdapterServices(private var categoryList: List<Result>, val context: Context) :
    RecyclerView.Adapter<AdapterServices.CategoryViewHolder>() {

    private var selectedPosition = -1

    inner class CategoryViewHolder(val binding: SingleRowServicesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            SingleRowServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categoryList.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CategoryViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = categoryList[position]

        holder.binding.tvName.text = item.category_name

        Glide.with(context)
            .load("https://groomers.co.in/public/uploads/${item.category_image}")
            .into(holder.binding.iamgeView)

        Log.e("ImageUrl", "https://groomers.co.in/public/uploads/${item.category_image}")

        // Highlight selected
        if (position == selectedPosition) {
            holder.binding.llHaircut.setBackgroundResource(R.drawable.selected_card)
        } else {
            holder.binding.llHaircut.setBackgroundResource(R.drawable.card_background)
        }

        holder.binding.llHaircut.setOnClickListener {
            selectedPosition = position
            serviceId = item.id.toString()
            serviceName = item.category_name
            notifyDataSetChanged()
        }
    }

    companion object {
        var serviceName = ""
        var serviceId = ""
    }

    @SuppressLint("NotifyDataSetChanged")
    fun selectedItemById(selectedId: String) {
        selectedPosition = categoryList.indexOfFirst { it.id.toString() == selectedId }
        if (selectedPosition != -1) {
            serviceId = categoryList[selectedPosition].id.toString()
            serviceName = categoryList[selectedPosition].category_name
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Result>, selectedCategoryId: String? = null) {
        categoryList = newList
        selectedPosition = selectedCategoryId?.let {
            categoryList.indexOfFirst { it.id.toString() == it.id.toString() }
        } ?: -1
        notifyDataSetChanged()
    }
}

