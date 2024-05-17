package com.example.dormitory_app.feature_booking_wm.presentation

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_app.R
import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class TimeRangeAdapter(
    private val date: String,
    private val clickListener: OnRecyclerItemClicked,
    private val wmNumber: Int
) : RecyclerView.Adapter<TimeRangeAdapter.TimeRangeViewHolder>() {
    private val list: MutableList<TimeRangeDto> = mutableListOf(
        TimeRangeDto(
            startTime = "08:00",
            endTime = "09:45",
            date = "date",
            wmNumber = wmNumber,
            withDrier = false,
            status = true
        ),
        TimeRangeDto(
            startTime = "09:45",
            endTime = "11:30",
            date = "date",
            wmNumber = wmNumber,
            withDrier = false,
            status = false
        ),
        TimeRangeDto(
            startTime = "11:30",
            endTime = "13:15",
            date = "date",
            wmNumber = wmNumber,
            withDrier = false,
            status = false
        ),
        TimeRangeDto(
            startTime = "13:15",
            endTime = "15:00",
            date = "date",
            wmNumber = wmNumber,
            withDrier = false,
            status = false
        ),
        TimeRangeDto(
            startTime = "15:00",
            endTime = "16:45",
            date = "date",
            wmNumber = wmNumber,
            withDrier = false,
            status = false
        ),
        TimeRangeDto(
            startTime = "16:45",
            endTime = "18:30",
            date = "date",
            wmNumber = wmNumber,
            withDrier = false,
            status = false
        ),
        TimeRangeDto(
            startTime = "18:30",
            endTime = "20:15",
            date = "date",
            wmNumber = wmNumber,
            withDrier = false,
            status = false
        ),
        TimeRangeDto(
            startTime = "20:15",
            endTime = "22:00",
            date = "date",
            wmNumber = wmNumber,
            withDrier = false,
            status = false
        ),
        TimeRangeDto(
            startTime = "22:00",
            endTime = "23:45",
            date = "date",
            wmNumber = wmNumber,
            withDrier = false,
            status = false
        )
    )
    fun setTimeRanges(ranges: List<TimeRangeDto>) {
        ranges.forEach {
            val index = mapTimeStartToIndex(it.startTime)
            list[index] = it.copy(
                startTime = it.startTime.split("T")[1],
                endTime = it.endTime.split("T")[1]
            )
            notifyItemChanged(index)
        }
    }

    private fun mapTimeStartToIndex(startTime: String) : Int {
        val preproc = startTime.split("T")
        val parsed = preproc[1].split(":")
        val hoursDelta = parsed[0].toInt() - 8
        val minutes = parsed[1].toInt()
        return (hoursDelta*60 + minutes) / 105
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TimeRangeAdapter.TimeRangeViewHolder {
        return TimeRangeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_timerange, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TimeRangeAdapter.TimeRangeViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class TimeRangeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTimeRange: TextView = itemView.findViewById(R.id.tvTimeRange)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val clTimeRange: ConstraintLayout = itemView.findViewById(R.id.clTimeRange)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: TimeRangeDto) {
            tvTimeRange.text = "${data.startTime}-${data.endTime}"
            if (!data.id.isNullOrBlank()) {
                tvStatus.text = "занято"
                clTimeRange.setBackgroundColor(itemView.context.resources.getColor(R.color.gray))
                tvStatus.setTextColor(itemView.context.resources.getColor(R.color.dark_gray))
                tvTimeRange.setTextColor(itemView.context.resources.getColor(R.color.dark_gray))
            } else {
                if (parseToLocalDateTimeFromString(data.startTime, date) < LocalDateTime.now(ZoneId.of("Europe/Moscow"))) {
                    tvStatus.text = "недоступно"
                    clTimeRange.setBackgroundColor(itemView.context.resources.getColor(R.color.gray))
                    tvStatus.setTextColor(itemView.context.resources.getColor(R.color.dark_gray))
                    tvTimeRange.setTextColor(itemView.context.resources.getColor(R.color.dark_gray))
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun parseToLocalDateTimeFromString(time: String, dt: String) : LocalDateTime {
            val q = LocalDate.now(ZoneId.of("Europe/Moscow"))
            val date = if (dt == "tomorrow") {
                q.plusDays(1)
            }
            else q
            val year = date.year
            val month = date.month
            val day = date.dayOfMonth
            val timeParts = time.split(":").map {it.toInt()}
            return LocalDateTime.of(year, month, day, timeParts[0], timeParts[1])
                .atZone(ZoneId.of("Europe/Moscow"))
                .toLocalDateTime()
        }
    }

    interface OnRecyclerItemClicked {
        fun onClick(timeRangeDto: TimeRangeDto)
    }
}