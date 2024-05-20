package com.example.minisofascore.ui.main_list.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.minisofascore.R
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Score
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.databinding.DayInfoLayoutBinding
import com.example.minisofascore.databinding.EndDividerBinding
import com.example.minisofascore.databinding.EventItemLayoutBinding
import com.example.minisofascore.databinding.SectionDividerBinding
import com.example.minisofascore.databinding.TournamentHeaderLayoutBinding
import com.google.android.material.color.MaterialColors
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class EventAdapter(private val onEventClick: (Event) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    private var items = emptyList<EventListItem>()

    fun updateItems(date: LocalDate, newItems: List<Event>) {

        val eventListItems = mutableListOf<EventListItem>()
        eventListItems.add(EventListItem.DayInfoItem(date, newItems.size))

        val eventsByTournament = newItems.groupBy { (it.tournament.id) }

        val numOfTournaments = eventsByTournament.keys.size
        var sectionCounter = 0

        for ((tournamentId, tournamentEvents) in eventsByTournament) {
            sectionCounter++

            val tournament = newItems.map { it.tournament }.find { it.id == tournamentId }
            tournament?.let { eventListItems.add(EventListItem.TournamentHeaderItem(it)) }

            val sortedEvents = tournamentEvents.sortedBy { it.startDate }

            eventListItems.addAll(sortedEvents.map { EventListItem.EventItem(it) })

            if (sectionCounter != numOfTournaments) {
                eventListItems.add(EventListItem.SectionDivider)
            }
        }
        eventListItems.add(EventListItem.EndDivider)
        items = eventListItems
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> EventInfoViewHolder(EventItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), onEventClick)
            1 -> TournamentHeaderViewHolder(TournamentHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            2 -> SectionDividerViewHolder(SectionDividerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            3 -> DayInfoViewHolder(DayInfoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            4 -> EndDividerViewHolder(EndDividerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is EventListItem.EventItem -> 0
            is EventListItem.TournamentHeaderItem -> 1
            is EventListItem.SectionDivider -> 2
            is EventListItem.DayInfoItem -> 3
            is EventListItem.EndDivider -> 4
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is EventListItem.EventItem -> (holder as EventInfoViewHolder).bind(item.event)
            is EventListItem.TournamentHeaderItem -> (holder as TournamentHeaderViewHolder).bind(item.tournament)
            is EventListItem.SectionDivider -> {}
            is EventListItem.DayInfoItem -> (holder as DayInfoViewHolder).bind(item.date, item.numOfEvents)
            is EventListItem.EndDivider -> {}
        }
    }

    class EventInfoViewHolder(
        private val binding: EventItemLayoutBinding,
        private val onEventClick: (Event) -> Unit
    ): ViewHolder(binding.root) {

        private val winnerTextColor = MaterialColors.getColor(binding.root, R.attr.on_surface_lv1, Color.BLACK)
        private val normalTextColor = MaterialColors.getColor(binding.root, R.attr.on_surface_lv2, Color.BLACK)
        private val liveColor = MaterialColors.getColor(binding.root, R.attr.live, Color.RED)
        fun bind(event: Event) {
            val startDateTime = Instant.ofEpochMilli(event.startDate.time).atZone(ZoneId.systemDefault()).toLocalDateTime()
            val startDate = startDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yy."))
            val startTime = startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            val isStartTimeToday = LocalDate.now() == startDateTime.toLocalDate()

            binding.root.setOnClickListener{
                onEventClick(event)
            }

            binding.teamHomeName.text = event.homeTeam.name
            binding.teamAwayName.text = event.awayTeam.name

            event.homeTeam.logo?.let { binding.teamHomeLogo.setImageBitmap(it) }
            event.awayTeam.logo?.let { binding.teamAwayLogo.setImageBitmap(it) }

            when (event.status) {
                "notstarted" -> {

                    if (isStartTimeToday) {
                        binding.startTime.text = startTime
                        binding.eventTime.text = "-"
                    } else {
                        binding.startTime.text = startDate
                        binding.eventTime.text = startTime
                    }
                    binding.eventTime.setTextColor(normalTextColor)

                    binding.teamHomeName.setTextColor(winnerTextColor)
                    binding.teamAwayName.setTextColor(winnerTextColor)

                    binding.teamHomeScore.text = ""
                    binding.teamAwayScore.text = ""

                }
                "inprogress" -> {

                    binding.startTime.text = startTime
                    val elapsedMinutesString = "${ChronoUnit.MINUTES.between(LocalDateTime.now(), startDateTime)}'"
                    binding.eventTime.text = elapsedMinutesString
                    binding.eventTime.setTextColor(liveColor)

                    binding.teamHomeName.setTextColor(winnerTextColor)
                    binding.teamAwayName.setTextColor(winnerTextColor)

                    binding.teamHomeScore.text = event.homeScore.getTotalAsString()
                    binding.teamAwayScore.text = event.awayScore.getTotalAsString()

                    binding.teamHomeScore.setTextColor(liveColor)
                    binding.teamAwayScore.setTextColor(liveColor)
                }

                "finished" -> {
                    if (isStartTimeToday) {
                        binding.startTime.text = startTime
                    } else {
                        binding.startTime.text = startDate
                    }
                    binding.eventTime.text = "FT"
                    binding.eventTime.setTextColor(normalTextColor)
                    var teamHomeColor: Int = normalTextColor
                    var teamAwayColor: Int = normalTextColor
                    if (event.winnerCode == "home") {
                        teamHomeColor = winnerTextColor
                    }
                    if (event.winnerCode == "away") {
                        teamAwayColor = winnerTextColor
                    }

                    binding.teamHomeScore.text = event.homeScore.getTotalAsString()
                    binding.teamAwayScore.text = event.awayScore.getTotalAsString()

                    binding.teamHomeName.setTextColor(teamHomeColor)
                    binding.teamHomeScore.setTextColor(teamHomeColor)
                    binding.teamAwayName.setTextColor(teamAwayColor)
                    binding.teamAwayScore.setTextColor(teamAwayColor)
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
    ): ViewHolder(binding.root)

    class DayInfoViewHolder(
        private val binding: DayInfoLayoutBinding
    ): ViewHolder(binding.root) {
        fun bind(date: LocalDate, numOfEvents: Int) {
            val isStartTimeToday = LocalDate.now() == date

            binding.dayInfoDate.text =
                if(isStartTimeToday) "Today"
                else date.format(DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy."))

            binding.dayInfoEvents.text =
            if (numOfEvents > 1) {
                "$numOfEvents Events"
            } else if (numOfEvents == 1) {
                "$numOfEvents Event"
            } else {
                "No Events"
            }
        }
    }

    class EndDividerViewHolder(
        binding: EndDividerBinding
    ): ViewHolder(binding.root)
}

fun Score.getTotalAsString(): String {
    val score = period1 + period2 + period3 + period4 + overtime
    return score.toString()
}