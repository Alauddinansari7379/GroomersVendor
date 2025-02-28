package com.groomers.groomersvendor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.groomers.groomersvendor.R
import java.text.SimpleDateFormat
import java.util.Locale


class OrderListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_list, container, false)
        /*view.findViewById<Button>(R.id.btn_930am).setOnClickListener {
            val intent = Intent(requireContext(), OrderLists::class.java)
            startActivity(intent)
        }
        view . findViewById < Button >(R.id.btn_930am).setOnClickListener {
            val intent = Intent(requireContext(), OrderLists::class.java)
            startActivity(intent)
        }
        view . findViewById < Button >(R.id.btn_1030am).setOnClickListener {
            val intent = Intent(requireContext(), OrderLists::class.java)
            startActivity(intent)
        }
        view . findViewById < Button >(R.id.btn_120pm).setOnClickListener {
            val intent = Intent(requireContext(), OrderLists::class.java)
            startActivity(intent)
        }
        view . findViewById < Button >(R.id.btn_510pm).setOnClickListener {
            val intent = Intent(requireContext(), OrderLists::class.java)
            startActivity(intent)
        }
        view . findViewById < Button >(R.id.btn_9pm).setOnClickListener {
            val intent = Intent(requireContext(), OrderLists::class.java)
            startActivity(intent)
        }
        view . findViewById < Button >(R.id.btn_10pm).setOnClickListener {
            val intent = Intent(requireContext(), OrderLists::class.java)
            startActivity(intent)
        }*/
        return view
    }
    private fun updateDateDisplay() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        tvDate.text = dateFormat.format(currentDate.time)
    }

}