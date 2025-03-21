package com.groomers.groomersvendor.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.groomers.groomersvendor.R

class TemplateAdapter(
    private val templates: List<Int>,
    private val onTemplateClick: (Int) -> Unit
) : RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        val frameLayout = FrameLayout(parent.context)
        return TemplateViewHolder(frameLayout)
    }

    override fun onBindViewHolder(holder: TemplateViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val templateRes = templates[position]

        // Inflate layout inside FrameLayout
        val view = LayoutInflater.from(holder.itemView.context)
            .inflate(templateRes, holder.frameLayout, false)

        holder.frameLayout.removeAllViews()
        holder.frameLayout.addView(view)

        // Highlight selected item
        if (position == selectedPosition) {
            holder.frameLayout.setBackgroundResource(R.drawable.selected_border) // Highlight
        } else {
            holder.frameLayout.setBackgroundResource(0) // Remove highlight
        }

        // Handle click event
        holder.frameLayout.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged() // Refresh RecyclerView to highlight selection
            onTemplateClick(templateRes)
        }
    }

    override fun getItemCount(): Int = templates.size

    class TemplateViewHolder(val frameLayout: FrameLayout) : RecyclerView.ViewHolder(frameLayout)
}
