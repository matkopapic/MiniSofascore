package com.example.minisofascore.ui.player_details.adapters

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.LastOrNext
import com.example.minisofascore.data.repository.PlayerRepository
import com.example.minisofascore.ui.tournament_matches.adapters.EventPagingSource
import kotlin.math.abs

class PlayerPagingSource(
    private val playerId: Int
) : EventPagingSource() {
    override suspend fun getEventPage(page: Int): List<Event> {
        // page >= 0 upcoming matches
        // page < 0 previous matches
        val lastOrNext = if (page < 0) LastOrNext.LAST else LastOrNext.NEXT

        // when page == 0 that is /next/0
        // when page == -1 that is /last/0
        val pageNum = if (page < 0) abs(page + 1) else page
        return when (val response = PlayerRepository.getPlayerEventPage(playerId, lastOrNext, pageNum)) {
            is Result.Success -> response.data
            is Result.Error -> throw Exception(response.error)
        }
    }
}