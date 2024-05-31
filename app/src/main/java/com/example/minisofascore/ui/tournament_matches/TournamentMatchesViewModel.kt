package com.example.minisofascore.ui.tournament_matches

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.minisofascore.data.repository.Repository
import com.example.minisofascore.ui.main_list.adapters.EventListItem
import com.example.minisofascore.ui.tournament_matches.adapters.EventPagingSource
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TournamentMatchesViewModel : ViewModel() {
    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

    private val repository = Repository()


    fun getEventPageFlow(tournamentId: Int) =
        Pager(
            PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
                )
        ) {
            EventPagingSource(tournamentId, repository)
        }.flow
            .map { pagingData -> pagingData.map { EventListItem.EventItem(it) } }
            .map {
                it.insertSeparators{ before, after ->
                    if (after != null && before?.event?.round != after.event.round) {
                        EventListItem.DayInfoItem(LocalDate.now(), 0, after.event.round)
                    } else {
                        null
                    }
            } }

}