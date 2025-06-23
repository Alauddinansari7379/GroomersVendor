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
import com.example.ehcf.Helper.currency
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.BookingDetailsActivity
import com.groomers.groomersvendor.databinding.BookingItemBinding
import com.groomers.groomersvendor.model.modelGetBooking.Result
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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
                tvPrice.text = currency + total.toString()
                tvCustomerName.text = customerName
                tvBookingStatues.text = status_name
                tvGender.text = user_type
                tvStartTime.text = start_time
                tvEndTime.text = end_time
                tvAddress.text = address
                tvDescription.text = description
                tvRating.text = rating.toString()

                @RequiresApi(Build.VERSION_CODES.O)
                when (slug) {
                    "waiting_for_accept" -> {
                        layoutAccept.visibility = View.VISIBLE
                        btnAccept.visibility = View.VISIBLE
                        btnReject.visibility = View.VISIBLE
                        btnComplete.visibility = View.GONE
                        btnReview.visibility = View.GONE
                        tvBookingStatues.background.setTint(ContextCompat.getColor(context, R.color.yellow))
                    }

                    "accepted" -> {
                        tvBookingStatues.background.setTint(ContextCompat.getColor(context, R.color.green))
                        if (isCurrentTimeGreater(date,end_time)){
                            layoutAccept.visibility = View.VISIBLE
                            btnAccept.visibility = View.GONE
                            btnReject.visibility = View.GONE
                            btnComplete.visibility = View.VISIBLE
                            if (comments == null){
                                btnReview.visibility = View.VISIBLE
                            }else{
                                btnReview.visibility = View.GONE
                            }
                        }

                    }

                    "rejected" -> {
                        tvBookingStatues.background.setTint(ContextCompat.getColor(context, R.color.red))
                        layoutAccept.visibility = View.GONE
                    }

                    "completed" -> {
                        tvBookingStatues.background.setTint(ContextCompat.getColor(context, R.color.green))
                        layoutAccept.visibility = View.GONE
                    }

                    else -> {
                        layoutAccept.visibility = View.GONE
                    }
                }
                root.setOnClickListener {
                    val intent = Intent(context, BookingDetailsActivity::class.java)
                    val booking = bookingList[position]

                    intent.putExtra("error", booking.error)
                    intent.putExtra("address", booking.address)
                    intent.putExtra("comments", booking.comments)
                    intent.putExtra("created_at", booking.created_at)
                    intent.putExtra("customerName", booking.customerName)
                    intent.putExtra("customer_comments", booking.customer_comments)
                    intent.putExtra("customer_id", booking.customer_id)
                    intent.putExtra("customer_rating", booking.customer_rating)
                    intent.putExtra("date", booking.date)
                    intent.putExtra("description", booking.description)
                    intent.putExtra("email", booking.email)
                    intent.putExtra("end_time", booking.end_time)
                    intent.putExtra("id", booking.id)
                    intent.putExtra("image", booking.image)
                    intent.putExtra("mobile", booking.mobile)
                    intent.putExtra("notes", booking.notes)
                    intent.putExtra("payment_mode", booking.payment_mode)
                    intent.putExtra("payment_name", booking.payment_name)
                    intent.putExtra("price", booking.price)
                    intent.putExtra("profile_picture", booking.profile_picture)
                    intent.putExtra("rating", booking.rating)
                    intent.putExtra("serviceName", booking.serviceName)
                    intent.putExtra("serviceType", booking.serviceType)
                    intent.putExtra("service_id", booking.service_id)
                    intent.putExtra("slot_id", booking.slot_id)
                    intent.putExtra("slug", booking.slug)
                    intent.putExtra("start_time", booking.start_time)
                    intent.putExtra("status", booking.status)
                    intent.putExtra("status_for_vendor", booking.status_for_vendor)
                    intent.putExtra("status_name", booking.status_name)
                    intent.putExtra("time", booking.time)
                    intent.putExtra("total", booking.total)
                    intent.putExtra("updated_at", booking.updated_at)
                    intent.putExtra("user_type", booking.user_type)
                    intent.putExtra("vendor_id", booking.vendor_id)

                    context.startActivity(intent)
                }

                btnAccept.setOnClickListener {
                   accept.accept(id.toString())
                }

                btnReject.setOnClickListener {
                   accept.reject(id.toString())
                }

                btnComplete.setOnClickListener {
                   accept.complete(id.toString())
                }

                btnReview.setOnClickListener {
                   accept.rating(id.toString())
                }

            }

        }

    }

    interface Accept {
        fun accept(bookingId: String)
        fun reject(bookingId: String)
        fun complete(bookingId: String)
        fun rating(bookingId: String)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isCurrentTimeGreater(dateString: String, endTimeString: String): Boolean {
        return try {
            val date = LocalDate.parse(dateString)           // Format: "yyyy-MM-dd"
            val endTime = LocalTime.parse(endTimeString)     // Format: "HH:mm:ss"

            val endDateTime = LocalDateTime.of(date, endTime)
            val currentDateTime = LocalDateTime.now()

            currentDateTime.isAfter(endDateTime)
        } catch (e: Exception) {
            e.printStackTrace()
            false // Return false if parsing fails
        }
    }

}