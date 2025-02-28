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
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.Login
import com.groomers.groomersvendor.activity.ManageSlots
import com.groomers.groomersvendor.activity.MySlot
import com.groomers.groomersvendor.activity.OrderLists
import com.groomers.groomersvendor.activity.Settings
import com.groomers.groomersvendor.databinding.FragmentProfileBinding
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    @Inject
    lateinit var sessionManager: SessionManager
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

            binding.cardCreateSlot.setOnClickListener {
                startActivity(Intent(requireContext(),ManageSlots::class.java))
            }
            binding.llSlotlist.setOnClickListener {
                startActivity(Intent(requireContext(),MySlot::class.java))
            }
            binding.cardLogout.setOnClickListener {
                SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure want to logout?")
                    .setCancelText("No")
                    .setConfirmText("Yes")
                    .showCancelButton(true)
                    .setConfirmClickListener { sDialog ->
                        sDialog.cancel()
                        sessionManager.clearSession()
                        val intent = Intent(requireContext(), Login::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        requireActivity().finish()
                        startActivity(intent)
                    }
                    .setCancelClickListener { sDialog ->
                        sDialog.cancel()
                    }
                    .show()
            }
    }

}