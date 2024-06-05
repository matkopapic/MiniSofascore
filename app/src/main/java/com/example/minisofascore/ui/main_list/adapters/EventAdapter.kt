package com.example.minisofascore.ui.main_list.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.minisofascore.R
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.EventStatus
import com.example.minisofascore.data.models.Score
import com.example.minisofascore.data.models.TeamSide
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.databinding.DayInfoLayoutBinding
import com.example.minisofascore.databinding.EventItemLayoutBinding
import com.example.minisofascore.databinding.SectionDividerBinding
import com.example.minisofascore.databinding.TournamentHeaderLayoutBinding
import com.example.minisofascore.util.getLocalDateTime
import com.example.minisofascore.util.loadTeamLogo
import com.example.minisofascore.util.loadTournamentLogo
import com.google.android.material.color.MaterialColors
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class EventAdapter(
    private val context: Context,
    private val onEventClick: (Event) -> Unit,
    private val onTournamentClick: (Tournament) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private var items = mutableListOf<EventListItem>()

    fun updateItems(date: LocalDate, newItems: List<Event>) {
        // handles adding tournament headers, section dividers, etc

        val eventListItems = mutableListOf<EventListItem>()

        // top element which displays date and number of events
        eventListItems.add(EventListItem.DayInfoItem(date, newItems.size))

        val eventsByTournament = newItems.groupBy { (it.tournament.id) }

        val numOfTournaments = eventsByTournament.keys.size
        var sectionCounter = 0

        for ((_, tournamentEvents) in eventsByTournament) {
            sectionCounter++

            val tournament = tournamentEvents[0].tournament
            eventListItems.add(EventListItem.TournamentHeaderItem(tournament))

            val sortedEvents = tournamentEvents.sortedBy { it.startDate }

            eventListItems.addAll(sortedEvents.map { EventListItem.EventItem(it) })

            // since we don't want to have section divider after last tournament
            if (sectionCounter != numOfTournaments) {
                eventListItems.add(EventListItem.SectionDivider)
            }
        }

        val diffResult = DiffUtil.calculateDiff(EventDiffCallBack(items, eventListItems))
        items = eventListItems
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            EventInfoViewHolder.TYPE -> EventInfoViewHolder(EventItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), context, onEventClick)
            TournamentHeaderViewHolder.TYPE -> TournamentHeaderViewHolder(TournamentHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), onTournamentClick)
            SectionDividerViewHolder.TYPE -> SectionDividerViewHolder(SectionDividerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            DayInfoViewHolder.TYPE -> DayInfoViewHolder(DayInfoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), context)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is EventListItem.EventItem -> EventInfoViewHolder.TYPE
            is EventListItem.TournamentHeaderItem -> TournamentHeaderViewHolder.TYPE
            is EventListItem.SectionDivider -> SectionDividerViewHolder.TYPE
            is EventListItem.DayInfoItem -> DayInfoViewHolder.TYPE
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is EventListItem.EventItem -> (holder as EventInfoViewHolder).bind(item.event)
            is EventListItem.TournamentHeaderItem -> (holder as TournamentHeaderViewHolder).bind(item.tournament)
            is EventListItem.SectionDivider -> {}
            is EventListItem.DayInfoItem -> (holder as DayInfoViewHolder).bind(item.date, item.numOfEvents)
        }
    }

    class EventInfoViewHolder(
        private val binding: EventItemLayoutBinding,
        private val context: Context,
        private val onEventClick: (Event) -> Unit
    ): ViewHolder(binding.root) {

        companion object {
            const val TYPE = 0
        }

        private val winnerTextColor = MaterialColors.getColor(binding.root, R.attr.on_surface_lv1, Color.BLACK)
        private val normalTextColor = MaterialColors.getColor(binding.root, R.attr.on_surface_lv2, Color.BLACK)
        private val liveColor = MaterialColors.getColor(binding.root, R.attr.live, Color.RED)
        fun bind(event: Event) {
            val startDateTime = event.startDate.getLocalDateTime()
            val startDate = startDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yy."))
            val startTime = startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            val isStartTimeToday = LocalDate.now() == startDateTime.toLocalDate()

            binding.root.setOnClickListener{
                onEventClick(event)
            }

            binding.teamHomeName.text = event.homeTeam.name
            binding.teamAwayName.text = event.awayTeam.name

            binding.teamHomeLogo.loadTeamLogo(event.homeTeam.id)
            binding.teamAwayLogo.loadTeamLogo(event.awayTeam.id)

            when (event.status) {
                EventStatus.NOT_STARTED -> {

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
                EventStatus.IN_PROGRESS -> {

                    binding.startTime.text = startTime

                    val elapsedMinutesString = "${ChronoUnit.MINUTES.between(startDateTime, LocalDateTime.now())}'"
                    binding.eventTime.text = elapsedMinutesString
                    binding.eventTime.setTextColor(liveColor)

                    binding.teamHomeName.setTextColor(winnerTextColor)
                    binding.teamAwayName.setTextColor(winnerTextColor)

                    binding.teamHomeScore.text = event.homeScore.getTotalAsString()
                    binding.teamAwayScore.text = event.awayScore.getTotalAsString()

                    binding.teamHomeScore.setTextColor(liveColor)
                    binding.teamAwayScore.setTextColor(liveColor)
                }

                EventStatus.FINISHED -> {
                    if (isStartTimeToday) {
                        binding.startTime.text = startTime
                    } else {
                        binding.startTime.text = startDate
                    }
                    binding.eventTime.text = context.getString(R.string.fulltime)
                    binding.eventTime.setTextColor(normalTextColor)
                    var teamHomeColor: Int = normalTextColor
                    var teamAwayColor: Int = normalTextColor
                    if (event.winnerCode == TeamSide.HOME) {
                        teamHomeColor = winnerTextColor
                    }
                    if (event.winnerCode == TeamSide.AWAY) {
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
        private val binding: TournamentHeaderLayoutBinding,
        private val onTournamentClick: (Tournament) -> Unit
    ): ViewHolder(binding.root) {

        companion object {
            const val TYPE = 1
        }

        fun bind(tournament: Tournament) {
            binding.root.setOnClickListener {
                onTournamentClick(tournament)
            }
            binding.countryName.text = tournament.country.name
            binding.tournamentName.text = tournament.name
            binding.tournamentLogo.loadTournamentLogo(tournament.id)
        }
    }

    class SectionDividerViewHolder(
        binding: SectionDividerBinding
    ): ViewHolder(binding.root) {
        companion object {
            const val TYPE = 2
        }
    }

    class DayInfoViewHolder(
        private val binding: DayInfoLayoutBinding,
        private val context: Context
    ): ViewHolder(binding.root) {

        companion object {
            const val TYPE = 3
        }
        fun bind(date: LocalDate, numOfEvents: Int) {
            val isStartTimeToday = LocalDate.now() == date

            binding.dayInfoDate.text =
                if (isStartTimeToday) context.getString(R.string.today)
                else date.format(DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy."))

            binding.dayInfoEvents.text = context.resources.getQuantityString(R.plurals.numOfEvents, numOfEvents, numOfEvents)

        }
    }
}

class EventDiffCallBack(
    private val oldList: List<EventListItem>,
    private val newList: List<EventListItem>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when {
            oldItem is EventListItem.TournamentHeaderItem
                    && newItem is EventListItem.TournamentHeaderItem -> {
                        oldItem.tournament.id == newItem.tournament.id
                    }
            oldItem is EventListItem.EventItem
                    && newItem is EventListItem.EventItem -> {
                        oldItem.event.id == newItem.event.id
                    }
            oldItem::class == newItem::class -> true
            else -> false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when {
            oldItem is EventListItem.TournamentHeaderItem
                    && newItem is EventListItem.TournamentHeaderItem -> {
                oldItem.tournament == newItem.tournament
            }
            oldItem is EventListItem.EventItem
                    && newItem is EventListItem.EventItem -> {
                oldItem.event == newItem.event
            }
            oldItem is EventListItem.DayInfoItem
                    && newItem is EventListItem.DayInfoItem -> {
                newItem.date == oldItem.date && newItem.numOfEvents == oldItem.numOfEvents
            }
            oldItem::class == newItem::class -> true
            else -> false
        }
    }

}

fun Score.getTotalAsString(): String {
    val score = period1 + period2 + period3 + period4 + overtime
    return score.toString()
}