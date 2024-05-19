package com.example.minisofascore.ui.home.adapters

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.minisofascore.R
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Score
import com.example.minisofascore.data.models.Tournament
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

class EventAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var items = emptyList<EventListItem>()

    fun updateItems(newItems: List<Event>) {

        val eventsByTournament = newItems.groupBy { (it.tournament.id) }


        val eventListItems = mutableListOf<EventListItem>()
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

    @RequiresApi(Build.VERSION_CODES.O)
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
        private val normalTextColor = MaterialColors.getColor(binding.root, R.attr.on_surface_lv2, Color.BLACK)
        private val liveColor = MaterialColors.getColor(binding.root, R.attr.live, Color.RED)
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(event: Event) {
            val startDateTime = Instant.ofEpochMilli(event.startDate.time).atZone(ZoneId.systemDefault()).toLocalDateTime()
            val startDate = startDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yy."))
            val startTime = startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            val isStartTimeToday = LocalDate.now() == startDateTime.toLocalDate()

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

                    binding.teamHomeScore.text = event.homeScore.getTotal()
                    binding.teamAwayScore.text = event.awayScore.getTotal()

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

                    binding.teamHomeScore.text = event.homeScore.getTotal()
                    binding.teamAwayScore.text = event.awayScore.getTotal()

                    binding.teamHomeName.setTextColor(teamHomeColor)
                    binding.teamHomeScore.setTextColor(teamHomeColor)
                    binding.teamAwayName.setTextColor(teamAwayColor)
                    binding.teamAwayScore.setTextColor(teamAwayColor)
                }
            }

//            binding.eventTime.text = when(event.status) {
//                "notstarted" -> "-"
//                "inprogress" -> ((System.currentTimeMillis() - event.startDate.time) / (1000 * 60) % 60).toString()
//                    .also { binding.eventTime.setTextColor(liveColor) }
//                "finished" -> "FT"
//                else -> "-"
//
//            }



//            if (event.status != "notstarted") {
//                val teamHomeScore = event.homeScore.period1 + event.homeScore.period2 +
//                        event.homeScore.period3 + event.homeScore.period4 + event.homeScore.overtime
//                binding.teamHomeScore.text = teamHomeScore.toString()
//
//                if (event.status == "inprogress") {
//                    binding.teamHomeScore.setTextColor(liveColor)
//                    binding.teamAwayScore.setTextColor(liveColor)
//                }
//
//                val teamAwayScore = event.awayScore.period1 + event.awayScore.period2 +
//                        event.awayScore.period3 + event.awayScore.period4 + event.awayScore.overtime
//                binding.teamAwayScore.text = teamAwayScore.toString()
//            } else {
//                binding.teamHomeScore.text = ""
//                binding.teamAwayScore.text = ""
//            }
//
//
//
//
//            when (event.winnerCode) {
//                "home" -> {
//                    binding.teamHomeName.setTextColor(winnerTextColor)
//                    binding.teamHomeScore.setTextColor(winnerTextColor)
//                }
//                "away" -> {
//                    binding.teamAwayName.setTextColor(winnerTextColor)
//                    binding.teamAwayScore.setTextColor(winnerTextColor)
//                }
//            }

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

fun Score.getTotal(): String {
    val score = period1 + period2 + period3 + period4 + overtime
    return score.toString()
}