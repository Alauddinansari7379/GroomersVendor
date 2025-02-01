package com.groomers.groomersvendor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.groomers.groomersvendor.databinding.ItemSliderBinding
import com.groomers.groomersvendor.model.SliderItem

class SliderAdapter(
    private val items: List<SliderItem>,
    private val viewPager: ViewPager2,
    private val onLastScreenClicked: () -> Unit
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(private val binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SliderItem, position: Int) {
            binding.tvWelcome.text = item.title
            binding.imgMascot.setImageResource(item.imageResId)

            if (position == items.size - 1) {
                binding.btnGetStarted.text = "GET STARTED"
                binding.btnGetStarted.setOnClickListener {
                    onLastScreenClicked() // Navigate to LoginActivity
                }
            } else {
                binding.btnGetStarted.text = "NEXT"
                binding.btnGetStarted.setOnClickListener {
                    viewPager.currentItem = position + 1 // Slide to next screen
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size
}
