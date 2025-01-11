package com.groomers.groomersvendor.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.OrderLists
import com.groomers.groomersvendor.activity.Settings


class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.findViewById<FloatingActionButton>(R.id.settings).setOnClickListener {
            val intent = Intent(requireContext(), Settings::class.java)
            startActivity(intent)
        }
        return view
    }

}