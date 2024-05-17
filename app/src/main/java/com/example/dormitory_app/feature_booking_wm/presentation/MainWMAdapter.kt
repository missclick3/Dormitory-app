package com.example.dormitory_app.feature_booking_wm.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_app.R
import com.example.dormitory_app.feature_booking_wm.messages.dtos.WMsForMainRecycler

class MainWMAdapter(
    private val date: String,
    private val clickListener: TimeRangeAdapter.OnRecyclerItemClicked
) : RecyclerView.Adapter<MainWMAdapter.WMViewHolder>() {
    val list: MutableList<WMsForMainRecycler> = mutableListOf()

    fun setWms(wms: List<WMsForMainRecycler>) {
        list.clear()
        list.addAll(wms)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainWMAdapter.WMViewHolder {
        return WMViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_wm_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainWMAdapter.WMViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class WMViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvWmNumber: TextView = itemView.findViewById(R.id.tvWmNumber)
        private val recyclerTimeRanges: RecyclerView = itemView.findViewById(R.id.recyclerTimeRanges)

        fun bind(data: WMsForMainRecycler) {
            tvWmNumber.text = "Стиральная машина №${data.wmNumber}"
            val adapter = TimeRangeAdapter(date, clickListener, data.wmNumber)
            adapter.setTimeRanges(data.ranges)
            recyclerTimeRanges.layoutManager = GridLayoutManager(itemView.context, 3)
            recyclerTimeRanges.adapter = adapter
        }
    }
}