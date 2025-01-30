package com.groomers.groomersvendor.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.OrderLists
import com.groomers.groomersvendor.activity.Settings
import com.groomers.groomersvendor.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding = FragmentProfileBinding.bind(view)
            binding.btnFloating.setOnClickListener {
                startActivity(Intent(requireContext(),Settings::class.java))
            }
    }

}