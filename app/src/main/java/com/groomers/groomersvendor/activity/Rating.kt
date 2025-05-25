package com.groomers.groomersvendor.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ehcf.Helper.myToast
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.MySlot.Companion.dayId
import com.groomers.groomersvendor.adapter.AdapterSlotsList
import com.groomers.groomersvendor.databinding.ActivityRatingBinding
import com.groomers.groomersvendor.helper.AppProgressBar
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.helper.NetworkChangeReceiver
import com.groomers.groomersvendor.helper.Toastic
import com.groomers.groomersvendor.model.modelslotlist.ModelSlotList
import com.groomers.groomersvendor.retrofit.ApiClient
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.RatingViewModel
import com.groomers.groomersvendor.viewmodel.SlotListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class Rating : AppCompatActivity() {
    private val context = this@Rating
    private lateinit var binding: ActivityRatingBinding
    lateinit var ratingBar: RatingBar
    lateinit var button: Button
    var meetingId = ""
    var rating = "1"
    var countN = 0
    private val viewModel: RatingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        meetingId = intent.getStringExtra("meetingId").toString()
        Log.e("meetingId", meetingId)

        binding.tvAddPhoto.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            val selectImage = 1234
            startActivityForResult(i, selectImage)
        }

        ratingBar = findViewById(R.id.ratingBar)
        ratingBar.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
        ratingBar.numStars = 5
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                this.rating = rating.toInt().toString()
                //  Toast.makeText(this@Rating, "Stars: " + rating.toInt(), Toast.LENGTH_SHORT).show()
            }
        binding.btnSendReview.setOnClickListener {
            val comment = binding.edtComment.text.toString()
            if (binding.edtComment.text.isEmpty()) {
                binding.edtComment.error = "Enter Your Review"
                return@setOnClickListener
            } else {
                lifecycleScope.launch {
                    viewModel.customerRating(meetingId, rating, comment)
                }
            }
        }
        // Observe isLoading to show/hide progress
        viewModel.isLoading.observe(this@Rating) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(this@Rating)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        }

        viewModel.modelRating.observe(this@Rating) { modelRating ->
            if (modelRating?.status == 1) {
                binding.edtComment.text.clear()
                Toastic.toastic(
                    context = this@Rating,
                    message = modelRating.message,
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.SUCCESS,
                    isIconAnimated = true,
                    textColor = if (false) Color.BLUE else null,
                ).show()
                finish()
            }
        }


        // Observe error message if login fails
        viewModel.errorMessage.observe(this@Rating) { errorMessage ->
            if (errorMessage!!.isNotEmpty()) {
                Toastic.toastic(
                    context = this@Rating,
                    message = errorMessage,
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.ERROR,
                    isIconAnimated = true,
                    textColor = if (false) Color.BLUE else null,
                ).show()
            }
        }

    }

//    override fun onStart() {
//        super.onStart()
//        CheckInternet().check { connected ->
//            if (connected) {
//
//                // myToast(requireActivity(),"Connected")
//            } else {
//                val changeReceiver = NetworkChangeReceiver(context)
//                changeReceiver.build()
//                //  myToast(requireActivity(),"Check Internet")
//            }
//        }
//    }
}


