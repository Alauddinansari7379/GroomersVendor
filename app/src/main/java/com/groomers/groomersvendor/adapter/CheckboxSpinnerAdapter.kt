package com.groomers.groomersvendor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.model.ModelDay

class CheckboxSpinnerAdapter(
    private val context: Context,
    private var daysList: MutableList<ModelDay>,
    private val onDaySelectionChanged: (List<ModelDay>) -> Unit // 🔁 callback
) : ArrayAdapter<ModelDay>(context, R.layout.item_checkbox_spinner, daysList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Show selected value in Spinner (not dropdown) – typically we don’t show checkboxes here
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getSelectedDaysText()

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_checkbox_spinner, parent, false)

        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val tvDay = view.findViewById<TextView>(R.id.tvDay)

        val modelDay = daysList[position]
        tvDay.text = modelDay.day

        // ⚠️ Detach listener before updating checkbox to prevent triggering it unintentionally
        checkBox.setOnCheckedChangeListener(null)
        checkBox.isChecked = modelDay.isSelected

        // ⚡ Real-time update on checkbox state change
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            modelDay.isSelected = isChecked
            // Notify the spinner to update the text immediately after checkbox state change
            notifyDataSetChanged()
        }

        return view
    }

    private fun getSelectedDaysText(): String {
        // Get selected days excluding "Select All"
        val selected = daysList.filter { it.isSelected && it.id != "0" }.map { it.day }
        return if (selected.isEmpty()) "Select Day(s)" else selected.joinToString(", ")
    }

    fun updateList(newList: MutableList<ModelDay>) {
        daysList = newList
        notifyDataSetChanged()
    }
}





