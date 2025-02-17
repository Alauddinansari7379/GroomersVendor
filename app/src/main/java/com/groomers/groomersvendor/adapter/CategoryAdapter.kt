package com.groomers.groomersvendor.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.groomers.groomersvendor.activity.About
import com.groomers.groomersvendor.databinding.CategoryCell1Binding
import com.groomers.groomersvendor.model.modelcategory.Result
import com.groomers.groomersvendor.viewmodel.MyApplication

class CategoryAdapter(private val categoryList : List<Result>, val context : Context):RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){
    inner class CategoryViewHolder(val binding : CategoryCell1Binding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CategoryCell1Binding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding);
    }
    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        with(categoryList[position]){
            holder.binding.tvCategory.text = category_name
            Glide.with(context)
                .load("https://groomers.co.in/public/uploads/"+category_image)
                .into(holder.binding.ivCategory)
            holder.binding.root.setOnClickListener{
                val intent = Intent(context, About::class.java)
                context.startActivity(intent)
            }
        }

    }
}