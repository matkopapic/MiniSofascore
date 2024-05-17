package com.example.minisofascore.ui.home.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.minisofascore.R
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.databinding.EventItemLayoutBinding
import com.google.android.material.color.MaterialColors
import java.util.Locale

class EventAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var items = emptyList<Event>()

    fun updateItems(newItems: List<Event>) {
        items = newItems
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> EventInfoViewHolder(EventItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is Event -> 0
            else -> 1
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is Event -> (holder as EventInfoViewHolder).bind(item)
        }
    }

    class EventInfoViewHolder(
        private val binding: EventItemLayoutBinding
    ): ViewHolder(binding.root) {

        private val winnerTextColor = MaterialColors.getColor(binding.root, R.attr.on_surface_lv1, Color.BLACK)
        private val liveColor = MaterialColors.getColor(binding.root, R.attr.live, Color.BLACK)
        fun bind(event: Event) {
            val startHour = event.startDate.time / (1000 * 60 * 60) % 24
            val startMinute = event.startDate.time / (1000 * 60) % 60
            val startTime = String.format(Locale.getDefault(),"%02d:%02d", startHour, startMinute)
            binding.startTime.text = startTime

            binding.eventTime.text = when(event.status) {
                "notstarted" -> "-"
                "inprogress" -> ((System.currentTimeMillis() - event.startDate.time) / (1000 * 60) % 60).toString()
                    .also { binding.eventTime.setTextColor(liveColor) }
                else -> "FT" // finished

            }

            binding.teamHomeName.text = event.homeTeam.name
            val teamHomeScore = event.homeScore.period1 + event.homeScore.period2 +
                    event.homeScore.period3 + event.homeScore.period4 + event.homeScore.overtime
            binding.teamHomeScore.text = teamHomeScore.toString()

            binding.teamAwayName.text = event.awayTeam.name
            val teamAwayScore = event.awayScore.period1 + event.awayScore.period2 +
                    event.awayScore.period3 + event.awayScore.period4 + event.awayScore.overtime
            binding.teamAwayScore.text = teamAwayScore.toString()

            event.homeTeam.logo?.let { binding.teamHomeLogo.setImageBitmap(it) }
            event.awayTeam.logo?.let { binding.teamAwayLogo.setImageBitmap(it) }

            when (event.winnerCode) {
                "home" -> {
                    binding.teamHomeName.setTextColor(winnerTextColor)
                    binding.teamHomeScore.setTextColor(winnerTextColor)
                }
                "away" -> {
                    binding.teamAwayName.setTextColor(winnerTextColor)
                    binding.teamAwayScore.setTextColor(winnerTextColor)
                }
            }

        }
    }


}