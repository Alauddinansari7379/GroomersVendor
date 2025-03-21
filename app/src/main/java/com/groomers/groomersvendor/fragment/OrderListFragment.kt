package com.groomers.groomersvendor.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.AddHelp
import com.groomers.groomersvendor.activity.CreateInstaTemplate
import com.groomers.groomersvendor.databinding.FragmentOrderListBinding


class OrderListFragment : Fragment() {
    lateinit var binding: FragmentOrderListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_order_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderListBinding.bind(view)
        binding.llInstagram.setOnClickListener {
startActivity(Intent(requireContext(),CreateInstaTemplate::class.java))
        }
        binding.cardGetHelp.setOnClickListener {
            startActivity(Intent(requireContext(), AddHelp::class.java))
        }
    }
}