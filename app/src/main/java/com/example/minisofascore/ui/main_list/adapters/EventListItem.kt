package com.example.minisofascore.ui.main_list.adapters

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Tournament
import java.time.LocalDate

sealed class EventListItem {
    data class EventItem(val event: Event) : EventListItem()
    data class TournamentHeaderItem(val tournament: Tournament) : EventListItem()

    data object SectionDivider : EventListItem()

    data class DayInfoItem(val date: LocalDate, val numOfEvents: Int): EventListItem()

}