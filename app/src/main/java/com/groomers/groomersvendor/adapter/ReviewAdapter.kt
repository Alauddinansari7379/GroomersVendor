package com.groomers.groomersvendor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.groomers.model.modelvendorrating.Result
import com.groomers.groomersvendor.databinding.ItemReviewBinding


class ReviewAdapter(private val reviews: List<Result>, val context: Context) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.binding.tvReviewerName.text = review.name
        holder.binding.ratingBarUser.rating = review.rating.toFloat()
        holder.binding.tvReviewText.text = review.comments
        val imageUrl = "https://groomers.co.in/public/uploads/${review.profile_picture}"
        Glide.with(context).load(imageUrl).into(holder.binding.ivProfile)
    }

    override fun getItemCount(): Int = reviews.size
}
