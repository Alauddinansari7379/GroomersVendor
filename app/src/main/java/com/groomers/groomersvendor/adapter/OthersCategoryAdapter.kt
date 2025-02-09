package com.groomers.groomersvendor.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.groomers.groomersvendor.activity.About
import com.groomers.groomersvendor.databinding.CategoryCellBinding
import com.groomers.groomersvendor.model.modelcategory.Result

class OthersCategoryAdapter(val categoryList : List<Result>, val context : Context): RecyclerView.Adapter<OthersCategoryAdapter.OthersCategoryViewHolder>(){
    inner class OthersCategoryViewHolder(val binding : CategoryCellBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OthersCategoryViewHolder {
        val binding = CategoryCellBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OthersCategoryViewHolder(binding);
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: OthersCategoryViewHolder, position: Int) {
        with(categoryList[position]){
            holder.binding.tvCategory.text = category_name
            holder.binding.card1.setOnClickListener{
                val intent = Intent(context, About::class.java)
                context.startActivity(intent)
            }
        }

    }
}