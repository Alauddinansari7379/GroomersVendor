package com.groomers.groomersvendor.fragment

import HomeAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.OrderDetail
import com.groomers.groomersvendor.activity.OrderLists
import com.groomers.groomersvendor.activity.Settings
import com.groomers.groomersvendor.databinding.FragmentHomeBinding
import com.groomers.groomersvendor.databinding.FragmentProfileBinding
import com.groomers.groomersvendor.model.Item
import java.io.IOException
import java.util.Locale


class HomeFragment : Fragment() {
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val REQUEST_CODE = 100
    private lateinit var currentLocation: TextView

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.llOrder.setOnClickListener {
            startActivity(Intent(requireContext(), OrderDetail::class.java))
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLastLocation()
        // Get the background color of the fragment's root view
        val backgroundColor = (view.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        (activity as? Common)?.updateStatusBarColor( backgroundColor)


    }
    @SuppressLint("SetTextI18n", "LogNotTimber")
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null && isAdded) {
                        try {
                            val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                            val addresses =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            Log.e(ContentValues.TAG, "addresses[0].latitude: ${addresses?.get(0)?.latitude}")
                            Log.e(ContentValues.TAG, "addresses[0].longitude: ${addresses?.get(0)?.longitude}")

                            addresses?.get(0)?.getAddressLine(0)

                            val locality = addresses?.get(0)?.locality
                            val countryName = addresses?.get(0)?.countryName
                            val countryCode = addresses?.get(0)?.countryCode
                            val postalCode = addresses?.get(0)?.postalCode.toString()
                            val subLocality = addresses?.get(0)?.subLocality
                            val subAdminArea = addresses?.get(0)?.subAdminArea
                           // currentAddress = "$subLocality, $locality, $countryName"
                            postalCodeNew = postalCode

                            // Update location text
                            binding.tvLocation.text = addresses?.get(0)?.getAddressLine(0)

                            Log.e(ContentValues.TAG, "locality: $locality")
                            Log.e(ContentValues.TAG, "countryName: $countryName")
                            Log.e(ContentValues.TAG, "countryCode: $countryCode")
                            Log.e(ContentValues.TAG, "postalCode: $postalCode")
                            Log.e(ContentValues.TAG, "subLocality: $subLocality")
                            Log.e(ContentValues.TAG, "subAdminArea: $subAdminArea")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
        } else {
            askPermission()
        }
    }


    companion object {
        var postalCodeNew = ""
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }
}
