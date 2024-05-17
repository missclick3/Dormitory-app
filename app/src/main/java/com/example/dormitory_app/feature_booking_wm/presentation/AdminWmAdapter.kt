package com.example.dormitory_app.feature_booking_wm.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_app.R
import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto
import com.example.dormitory_app.feature_booking_wm.messages.dtos.WashingMachineDto

class AdminWmAdapter(
    private val deleteListener: OnRecyclerItemClicked,
    private val changeStatusListener: OnRecyclerItemClicked
) : RecyclerView.Adapter<AdminWmAdapter.AdminWmViewHolder>() {
    val list: MutableList<WashingMachineDto> = mutableListOf()

    fun setWms(wms: List<WashingMachineDto>) {
        list.clear()
        list.addAll(wms.sortedBy { it.wmNumber })
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminWmAdapter.AdminWmViewHolder {
        return AdminWmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_wm_admin, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AdminWmAdapter.AdminWmViewHolder, position: Int) {
        holder.bind(list[position])
        holder.btnDeleteWm.setOnClickListener {
            deleteListener.onClick(list[position])
        }
        holder.btnChangeWmStatus.setOnClickListener {
            changeStatusListener.onClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class AdminWmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAdminWmNumber: TextView = itemView.findViewById(R.id.tvAdminWmNumber)
        val tvWmAdminStatusValue: TextView = itemView.findViewById(R.id.tvWmAdminStatusValue)
        val btnChangeWmStatus: Button = itemView.findViewById(R.id.btnChangeWmStatus)
        val btnDeleteWm: Button = itemView.findViewById(R.id.btnDeleteWm)

        fun bind(washingMachineDto: WashingMachineDto) {
            tvAdminWmNumber.text = washingMachineDto.wmNumber.toString()
            if (!washingMachineDto.enabled) {
                tvWmAdminStatusValue.text = "выключена"
                tvWmAdminStatusValue.setTextColor(android.graphics.Color.RED)
            } else {
                tvWmAdminStatusValue.text = "активна"
                tvWmAdminStatusValue.setTextColor(android.graphics.Color.GREEN)
            }
        }
    }

    interface OnRecyclerItemClicked {
        fun onClick(washingMachineDto: WashingMachineDto)
    }
}