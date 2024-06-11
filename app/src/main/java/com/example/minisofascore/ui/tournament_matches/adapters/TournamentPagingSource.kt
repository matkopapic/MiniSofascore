package com.example.minisofascore.ui.tournament_matches.adapters

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.repository.LastOrNext
import kotlin.math.abs
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.TournamentRepository

class TournamentPagingSource (
    private val tournamentId: Int
): EventPagingSource() {
        override suspend fun getEventPage(page: Int): List<Event> {
            // page >= 0 upcoming matches
            // page < 0 previous matches
            val lastOrNext = if (page < 0) LastOrNext.LAST else LastOrNext.NEXT

            // when page == 0 that is /next/0
            // when page == -1 that is /last/0
            val pageNum = if (page < 0) abs(page + 1) else page
            return when (val response = TournamentRepository.getTournamentEventPage(tournamentId, lastOrNext, pageNum)) {
                is Result.Success -> response.data
                is Result.Error -> throw Exception(response.error)
            }
        }
}