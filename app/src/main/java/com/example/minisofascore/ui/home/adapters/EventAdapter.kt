package com.example.minisofascore.ui.home.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.minisofascore.R
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.databinding.EventItemLayoutBinding
import com.example.minisofascore.databinding.SectionDividerBinding
import com.example.minisofascore.databinding.TournamentHeaderLayoutBinding
import com.google.android.material.color.MaterialColors
import java.util.Locale

class EventAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var items = emptyList<EventListItem>()

    fun updateItems(newItems: List<Event>) {
        // Group events by tournament ID
        val eventsByTournament = newItems.groupBy { (it.tournament.id) }

        // Create a list of EventListItem objects
        val eventListItems = mutableListOf<EventListItem>()
        val numOfTournaments = eventsByTournament.keys.size
        var sectionCounter = 0
        // Iterate over the grouped events
        for ((tournamentId, tournamentEvents) in eventsByTournament) {
            sectionCounter++
            // Add a tournament header item
            val tournament = newItems.map { it.tournament }.find { it.id == tournamentId }
            tournament?.let { eventListItems.add(EventListItem.TournamentHeaderItem(it)) }


            // Sort events by date
            val sortedEvents = tournamentEvents.sortedBy { it.startDate }

            // Add event items
            eventListItems.addAll(sortedEvents.map { EventListItem.EventItem(it) })

            if (sectionCounter != numOfTournaments) {
                eventListItems.add(EventListItem.SectionDivider)
            }
        }
        items = eventListItems
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> EventInfoViewHolder(EventItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            1 -> TournamentHeaderViewHolder(TournamentHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            2 -> SectionDividerViewHolder(SectionDividerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is EventListItem.EventItem -> 0
            is EventListItem.TournamentHeaderItem -> 1
            is EventListItem.SectionDivider -> 2
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is EventListItem.EventItem -> (holder as EventInfoViewHolder).bind(item.event)
            is EventListItem.TournamentHeaderItem -> (holder as TournamentHeaderViewHolder).bind(item.tournament)
            is EventListItem.SectionDivider -> (holder as SectionDividerViewHolder).bind()
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

    class TournamentHeaderViewHolder(
        private val binding: TournamentHeaderLayoutBinding
    ): ViewHolder(binding.root) {

        fun bind(tournament: Tournament) {
            binding.countryName.text = tournament.country.name
            binding.tournamentName.text = tournament.name
            tournament.logo?.let { binding.tournamentLogo.setImageBitmap(it) }
        }
    }

    class SectionDividerViewHolder(
        binding: SectionDividerBinding
    ): ViewHolder(binding.root) {

        fun bind(){

        }
    }
}