package com.groomers.groomersvendor.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.convertTo12Hour
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.model.modelslotlist.ModelSlotList

class AdapterSlotsList(
    val context: Context, private val list: ModelSlotList

    ) :
    RecyclerView.Adapter<AdapterSlotsList.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_slot_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.slotId.text = "${position + 1}"
        holder.startTime.text = convertTo12Hour(list.result[position].start_time)
        holder.endTime.text = convertTo12Hour(list.result[position].end_time)
        // holder.slotId.text= list.result[position].id.toString()
        holder.address.text = list.result[position].address?.toString()

        when (list.result[position].day) {
            "1" -> {
                holder.day.text = "Monday"
            }

            "2" -> {
                holder.day.text = "Tuesday"
            }

            "3" -> {
                holder.day.text = "Wednesday"
            }

            "4" -> {
                holder.day.text = "Thursday"
            }

            "5" -> {
                holder.day.text = "Friday"
            }

            "6" -> {
                holder.day.text = "Saturday"
            }

            "7" -> {
                holder.day.text = "Sunday"
            }
        }


        holder.switchActive.setOnClickListener {
            if (holder.switchActive.isChecked) {
                val active = "1"

                holder.switchActive.text = "Active"
            } else {
                val active = "0"
                holder.switchActive.text = "Inactive"

            }
        }





        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var startTime: Button = itemView.findViewById(R.id.btnStartTimeSlotL);
        var address: TextView = itemView.findViewById(R.id.tvAddress)
        var endTime: Button = itemView.findViewById(R.id.btnEndTimeSlotL);
        var delete: TextView = itemView.findViewById(R.id.imgDelete);
        var slotId: TextView = itemView.findViewById(R.id.tvSlotNumber);
        var day: TextView = itemView.findViewById(R.id.tvDaySlotList);
        var consultationTypeMySlot: TextView = itemView.findViewById(R.id.tvConsultationTypeMySlot);
        var cardView: CardView = itemView.findViewById(R.id.cardSlotList);
        var btnupdate: Button = itemView.findViewById(R.id.btnUpdateSlotList);
        var switchActive: Switch = itemView.findViewById(R.id.switchActive)
        var layoutAddress: LinearLayout = itemView.findViewById(R.id.layoutAddress)

    }
}