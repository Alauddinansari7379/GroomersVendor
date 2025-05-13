package com.groomers.groomersvendor.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
            startActivity(Intent(requireContext(), CreateInstaTemplate::class.java))
        }
        binding.cardGetHelp.setOnClickListener {
            startActivity(Intent(requireContext(), AddHelp::class.java))
        }

        binding.cardVisibility.setOnClickListener {
            openDialog()
        }
    }

    private fun openDialog() {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)

            val emailLayout = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }
            val mobileLayout = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }

            val emailText = TextView(context).apply { text = "Email: example@mail.com" }
            val emailCopyIcon = ImageView(context).apply {
                setImageResource(R.drawable.baseline_content_copy_24)
                layoutParams = LinearLayout.LayoutParams(50, 50).apply { // Making the icon smaller
                    setMargins(20, 0, 0, 0) // Adding margin between text and icon
                }
                setOnClickListener {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Email", emailText.text.toString().removePrefix("Email: ")))
                    Toast.makeText(context, "Email copied!", Toast.LENGTH_SHORT).show()
                }
            }

            val mobileText = TextView(context).apply { text = "Mobile: +91-1234567890" }
            val mobileCopyIcon = ImageView(context).apply {
                setImageResource(R.drawable.baseline_content_copy_24)
                layoutParams = LinearLayout.LayoutParams(50, 50).apply { // Making the icon smaller
                    setMargins(20, 0, 0, 0) // Adding margin between text and icon
                }
                setOnClickListener {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Mobile", mobileText.text.toString().removePrefix("Mobile: ")))
                    Toast.makeText(context, "Mobile number copied!", Toast.LENGTH_SHORT).show()
                }
            }

            val addressText = TextView(context).apply { text = "Address: Hyderabad, India" }

            emailLayout.addView(emailText)
            emailLayout.addView(emailCopyIcon)
            mobileLayout.addView(mobileText)
            mobileLayout.addView(mobileCopyIcon)

            addView(emailLayout)
            addView(mobileLayout)
            addView(addressText)
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Connect Information")
            .setView(layout)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

}