package com.example.minisofascore.ui.player_details

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.minisofascore.ui.main_list.adapters.EventListItem
import com.example.minisofascore.ui.player_details.adapters.PlayerPagingSource
import kotlinx.coroutines.flow.map

class PlayerDetailsViewModel : ViewModel() {
    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

    fun getEventPageFlow(teamId: Int) =
        Pager(
            PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            )
        ) {
            PlayerPagingSource(teamId)
        }.flow
            .map { pagingData -> pagingData.map { EventListItem.EventItem(it) } }
            .map {
                it.insertSeparators{ before, after ->
                    if (after != null && before?.event?.tournament != after.event.tournament) {
                        EventListItem.TournamentHeaderItem(after.event.tournament)
                    } else {
                        null
                    }
                } }
}