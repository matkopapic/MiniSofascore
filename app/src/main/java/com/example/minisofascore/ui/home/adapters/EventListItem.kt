package com.example.minisofascore.ui.home.adapters

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Tournament

sealed class EventListItem {
    data class EventItem(val event: Event) : EventListItem()
    data class TournamentHeaderItem(val tournament: Tournament) : EventListItem()

    data object SectionDivider : EventListItem()
}