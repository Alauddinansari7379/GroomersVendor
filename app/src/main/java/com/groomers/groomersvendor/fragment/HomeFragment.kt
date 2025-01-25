package com.groomers.groomersvendor.fragment

import HomeAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
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
import com.groomers.groomersvendor.model.Item
import java.io.IOException
import java.util.Locale


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView
    private lateinit var recyclerView4: RecyclerView
    private lateinit var recyclerView5: RecyclerView
    private lateinit var recyclerView6: RecyclerView
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var currentAddress = ""
    private val REQUEST_CODE = 100
    private lateinit var currentLocation: TextView


    private lateinit var adapter: HomeAdapter
    private val itemList = listOf(
        Item(R.drawable.hair_cutting, "Hair Cut"),
        Item(R.drawable.hair_salon, "Hair Spa"),
        Item(R.drawable.body_message, "Body Massage"),
        Item(R.drawable.hair_cutting1, "Hair Coloring"),
        Item(R.drawable.body_message, "Scalp Treatment"),
        Item(R.drawable.body_message, "Aromatherapy Massage"),
        Item(R.drawable.body_message, "Facial"),
        Item(R.drawable.hair_cutting, "Manicure"),
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = HomeAdapter(itemList)
        recyclerView.adapter = adapter
        currentLocation = view.findViewById<TextView>(R.id.tvLocation)
        recyclerView2 = view.findViewById(R.id.recyclerView2)
        recyclerView2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = HomeAdapter(itemList)
        recyclerView2.adapter = adapter


        recyclerView3 = view.findViewById(R.id.recyclerView3)
        recyclerView3.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = HomeAdapter(itemList)
        recyclerView3.adapter = adapter


        recyclerView4 = view.findViewById(R.id.recyclerView4)
        recyclerView4.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = HomeAdapter(itemList)
        recyclerView4.adapter = adapter


        recyclerView5 = view.findViewById(R.id.recyclerView5)
        recyclerView5.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = HomeAdapter(itemList)
        recyclerView5.adapter = adapter


        recyclerView6 = view.findViewById(R.id.recyclerView6)
        recyclerView6.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = HomeAdapter(itemList)
        recyclerView6.adapter = adapter
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
     //   getLastLocation()
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
                            currentAddress = "$subLocality, $locality, $countryName"
                            postalCodeNew = postalCode

                            // Update location text
                            currentLocation.text = addresses?.get(0)?.getAddressLine(0)

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
