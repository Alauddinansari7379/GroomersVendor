package com.groomers.groomersvendor.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.groomers.groomersvendor.databinding.HelpRowBinding
import com.groomers.groomersvendor.model.modelhelplist.Result

class AdapterHelpList(private var categoryList : List<Result>, val context : Context):RecyclerView.Adapter<AdapterHelpList.PopularViewHolder>(){
    inner class PopularViewHolder(val binding : HelpRowBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = HelpRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PopularViewHolder(binding);
    }
    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        with(categoryList[position]) {
            holder.binding.tvName.text = name
            holder.binding.tvQuery.text = query
            holder.binding.tvMobile.text = mobile.toString()
            holder.binding.tvDescription.text = description
            Glide.with(context)
                .load("https://groomers.co.in/public/uploads/" + image)
                .into(holder.binding.ivHelpImage)


        }

    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Result>) {
        categoryList = newList
        notifyDataSetChanged()
    }
}