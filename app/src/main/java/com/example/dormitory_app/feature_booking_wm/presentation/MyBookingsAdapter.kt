package com.example.dormitory_app.feature_booking_wm.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_app.R
import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto
import com.example.dormitory_app.feature_booking_wm.messages.dtos.WashingMachineDto

class MyBookingsAdapter(
    private val clickListener: OnRecyclerItemClicked
) : RecyclerView.Adapter<MyBookingsAdapter.MyBookingsViewHolder>() {
    private val list: MutableList<TimeRangeDto>  = mutableListOf()

    fun setBookings(bookings: List<TimeRangeDto>) {
        list.clear()
        list.addAll(bookings)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyBookingsAdapter.MyBookingsViewHolder {
        return MyBookingsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_my_bookings, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyBookingsAdapter.MyBookingsViewHolder, position: Int) {
        holder.bind(list[position])
        holder.btnCancelBooking.setOnClickListener {
            clickListener.onClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyBookingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMyWmValue: TextView = itemView.findViewById(R.id.tvMyWmValue)
        val tvMyTimeValue: TextView = itemView.findViewById(R.id.tvMyTimeValue)
        val tvMyStatusValue: TextView = itemView.findViewById(R.id.tvMyStatusValue)
        val tvMyDrierValue: TextView = itemView.findViewById(R.id.tvMyDrierValue)

        val btnCancelBooking: Button = itemView.findViewById(R.id.btnCancelBooking)

        fun bind(timeRangeDto: TimeRangeDto) {
            tvMyWmValue.text = timeRangeDto.wmNumber.toString()
            tvMyTimeValue.text = "${timeRangeDto.startTime.split("T")[1]}-${timeRangeDto.endTime.split("T")[1]}"
            if (timeRangeDto.status) {
                tvMyStatusValue.text = "активна"
                tvMyStatusValue.setTextColor(Color.GREEN)
            } else {
                tvMyStatusValue.text = "отключена"
                tvMyStatusValue.setTextColor(Color.RED)
            }
            tvMyDrierValue.text = if(timeRangeDto.withDrier) "да" else "нет"
        }
    }

    interface OnRecyclerItemClicked {
        fun onClick(timeRangeDto: TimeRangeDto)
    }
}