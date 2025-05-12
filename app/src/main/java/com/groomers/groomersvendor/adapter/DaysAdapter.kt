package com.groomers.groomersvendor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.groomers.groomersvendor.databinding.DaysRowBinding
import com.groomers.groomersvendor.model.ModelDay

class DaysAdapter(
    private val context: Context,
    private var selectedDaysList: MutableList<ModelDay>,
    private val onDayRemoved: (ModelDay) -> Unit
) : RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {

    inner class DaysViewHolder(val binding: DaysRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val binding = DaysRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DaysViewHolder(binding)
    }

    override fun getItemCount(): Int = selectedDaysList.size

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val selectedDay = selectedDaysList[position]
        holder.binding.tvDay.text = selectedDay.day

        holder.binding.ivClose.setOnClickListener {
            onDayRemoved(selectedDay) // ✅ Notify parent to handle safe removal
        }
    }

    fun updateList(newList: MutableList<ModelDay>) {
        selectedDaysList = newList
        notifyDataSetChanged()
    }
}




