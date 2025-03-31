import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ItemCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(
    private val dates: List<Date>,
    private val today: Calendar,
    private val onDateClick: (String) -> Unit // Callback function to return clicked date
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var selectedPosition: Int = getTodayPosition() // Default to today’s position

    inner class CalendarViewHolder(val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val date = dates[position]
        val calendar = Calendar.getInstance().apply { time = date }
        val isToday = isToday(calendar) // Use local variable to check if this is today

        with(holder.binding) {
            tvDate.text = SimpleDateFormat("dd", Locale.getDefault()).format(date)
            tvDay.text = SimpleDateFormat("EEE", Locale.getDefault()).format(date) // Display day (e.g., Mon, Tue)

            // Highlight selected date, unhighlight today if another date is selected
            if (position == selectedPosition) {
                tvDate.setTextColor(Color.WHITE)
                tvDay.setTextColor(Color.WHITE)
                layoutMain.setBackgroundColor(
                    ContextCompat.getColor(root.context, R.color.mainColor)
                )
            } else {
                tvDate.setTextColor(Color.BLACK)
                tvDay.setTextColor(Color.BLACK)
                layoutMain.setBackgroundColor(Color.TRANSPARENT)
            }

            // Handle click event
            root.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = position
                onDateClick(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)) // Pass clicked date

                // Un-highlight previous selection and refresh UI
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
            }
        }
    }

    private fun isToday(calendar: Calendar): Boolean {
        return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
    }

    fun getTodayPosition(): Int {
        return dates.indexOfFirst { date ->
            val calendar = Calendar.getInstance().apply { time = date }
            isToday(calendar)
        }.takeIf { it != -1 } ?: 0
    }

    override fun getItemCount(): Int = dates.size
}
