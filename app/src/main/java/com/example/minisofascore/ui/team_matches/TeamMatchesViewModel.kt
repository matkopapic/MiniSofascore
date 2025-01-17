package com.example.minisofascore.ui.team_matches

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.minisofascore.ui.main_list.adapters.EventListItem
import com.example.minisofascore.ui.team_matches.adapters.TeamPagingSource
import kotlinx.coroutines.flow.map

class TeamMatchesViewModel : ViewModel() {
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
            TeamPagingSource(teamId)
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