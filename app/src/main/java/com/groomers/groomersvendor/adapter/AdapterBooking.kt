package com.groomers.groomersvendor.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.About
import com.groomers.groomersvendor.databinding.BookingItemBinding
import com.groomers.groomersvendor.model.modelGetBooking.Result
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso

class AdapterBooking(val bookingList: List<Result>, val context: Context, val accept: Accept) :
    RecyclerView.Adapter<AdapterBooking.BookingViewHolder>() {
    inner class BookingViewHolder(val binding: BookingItemBinding) : ViewHolder(binding.root)

    lateinit var sessionManager: SessionManager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = BookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding);
    }

    override fun getItemCount(): Int = bookingList.size

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        sessionManager = SessionManager(context)
        with(bookingList[position]) {
            with(holder.binding) {
                if (bookingList[position].profile_picture != null) {
                    Picasso.get()
                        .load("${sessionManager.imageUrl}${bookingList[position].profile_picture}")
                        .error(R.drawable.error_image)
                        .into(imageView)
                } else {
                    imageView.setImageResource(R.drawable.error_image) // Correct method to set the image
                }


                tvServiceName.text = serviceName
                tvDate.text = date
                tvPrice.text = "$" + total.toString()
                tvCustomerName.text = customerName
                tvBookingStatues.text = status_name
                tvGender.text = user_type
                tvStartTime.text = start_time
                tvEndTime.text = end_time
                tvAddress.text = address
                tvDescription.text = description
                tvRating.text = rating.toString()


                when (slug) {
                    "waiting_for_accept" -> {
                        layoutAccept.visibility = View.VISIBLE
                        tvBookingStatues.background.setTint(ContextCompat.getColor(context, R.color.yellow))
                    }

                    "accepted" -> {
                        tvBookingStatues.background.setTint(ContextCompat.getColor(context, R.color.green))
                        layoutAccept.visibility = View.GONE
                    }

                    "rejected" -> {
                        tvBookingStatues.background.setTint(ContextCompat.getColor(context, R.color.red))
                        layoutAccept.visibility = View.GONE
                    }

                    else -> {
                        layoutAccept.visibility = View.GONE
                    }
                }


                //                card.setOnClickListener {
//                    val intent = Intent(context, About::class.java)
//                    context.startActivity(intent)
//                }
            }

        }

    }

    interface Accept {
        fun accept(bookingId: String)

    }
}