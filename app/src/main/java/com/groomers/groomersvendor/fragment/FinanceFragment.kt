package com.groomers.groomersvendor.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.BankInformation
import com.groomers.groomersvendor.databinding.FragmentFinanceBinding
import com.groomers.groomersvendor.databinding.FragmentProfileBinding
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import javax.inject.Inject

class FinanceFragment : Fragment() {
    private lateinit var binding: FragmentFinanceBinding

    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager.isBank

        with(binding) {
            if (sessionManager.isBank != "1") {
                setupNowButton.visibility = View.GONE
            }
            setupNowButton.setOnClickListener {
                startActivity(Intent(requireContext(), BankInformation::class.java))
            }
        }

    }
}