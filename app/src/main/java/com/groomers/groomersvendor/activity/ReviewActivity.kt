package com.groomers.groomersvendor.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.groomers.model.modelvendorrating.Result
import com.groomers.groomersvendor.adapter.ReviewAdapter
import com.groomers.groomersvendor.databinding.ActivityReviewBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.helper.Toastic
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.RatingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReviewActivity : AppCompatActivity() {
    private val binding by lazy { ActivityReviewBinding.inflate(layoutInflater) }
    private val viewModel: RatingViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            viewModel.getAllVendorRatings(sessionManager.vendorId.toString())
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(this)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        }

        viewModel.modelVendorRating.observe(this) { modelRating ->
            if (modelRating?.status == 1) {
                val overAllRating = calculateOverallRating(modelRating.result).toString()
                val counts = getStarRatingCounts(modelRating.result)
                updateRatingProgressBars(counts, modelRating.result.size)
                binding.ratingBarAverage.text = overAllRating
                binding.ratingBarDProfile.rating = overAllRating.toFloat()
                binding.tvReviewCount.text = modelRating.result.size.toString()
                binding.rvReviewList.apply {
                    adapter = ReviewAdapter(modelRating.result, this@ReviewActivity)
                }

            }
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toastic.toastic(
                    context = this,
                    message = errorMessage,
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.ERROR,
                    isIconAnimated = true,
                    textColor = null // Set to a specific color if needed
                ).show()

            }
        }

        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    private fun calculateOverallRating(reviews: List<Result>): Float {
        if (reviews.isEmpty()) return 0f
        val totalRating = reviews.sumOf { it.rating }
        return totalRating.toFloat() / reviews.size
    }


    fun getStarRatingCounts(reviews: List<Result>): Map<Int, Int> {
        val starCounts = mutableMapOf(
            1 to 0,
            2 to 0,
            3 to 0,
            4 to 0,
            5 to 0
        )

        for (review in reviews) {
            val rating = review.rating.toInt()
            if (rating in 1..5) {
                starCounts[rating] = starCounts[rating]!! + 1
            }
        }

        return starCounts
    }

    private fun updateRatingProgressBars(
        counts: Map<Int, Int>,
        totalReviews: Int
    ) {
        val progressBars = mapOf(
            5 to binding.pbStar5,
            4 to binding.pbStar4,
            3 to binding.pbStar3,
            2 to binding.pbStar2,
            1 to binding.pbStar1
        )

        val countLabels = mapOf(
            5 to binding.tvStarCount5,
            4 to binding.tvStarCount4,
            3 to binding.tvStarCount3,
            2 to binding.tvStarCount2,
            1 to binding.tvStarCount1
        )

        for (star in 1..5) {
            val count = counts[star] ?: 0
            val percent = if (totalReviews > 0) (count * 100 / totalReviews) else 0

            progressBars[star]?.progress = percent
            countLabels[star]?.text = count.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearData()
    }
}