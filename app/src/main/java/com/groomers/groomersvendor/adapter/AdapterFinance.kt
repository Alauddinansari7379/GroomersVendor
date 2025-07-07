package com.groomers.groomersvendor.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.changeDateFormat6
import com.example.ehcf.Helper.currency
import com.groomers.groomersvendor.databinding.SingleRowFinanceBinding
import com.groomers.groomersvendor.model.modelEarning.Earning
import com.groomers.groomersvendor.model.modelEarning.Result

class AdapterFinance(
    val context: Context,
    private val items: Result,
) : RecyclerView.Adapter<AdapterFinance.SliderViewHolder>() {

    inner class SliderViewHolder(private val binding: SingleRowFinanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: List<Earning>, position: Int) {

            for (i in item) {
                binding.tvName.text = i.name
                binding.tvDate.text = changeDateFormat6(i.created_at)
                binding.tvAmt.text = currency + i.amount.toString()
                binding.tvServiceType.text = i.serviceName
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding =
            SingleRowFinanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(items.today_order, position)
    }

    override fun getItemCount(): Int = items.today_order.size
}
