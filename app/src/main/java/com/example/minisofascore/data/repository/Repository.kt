package com.example.minisofascore.data.repository

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.TeamSide
import com.example.minisofascore.data.remote.BASE_URL
import com.example.minisofascore.data.remote.Network
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.util.safeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Repository {
    private val api = Network.getInstance()

    companion object {
        fun getTeamLogoUrl(teamId: Int) = "${BASE_URL}team/$teamId/image"
        fun getTournamentLogoUrl(tournamentId: Int) = "${BASE_URL}tournament/$tournamentId/image"
    }

    suspend fun getIncidentsForEvent(eventId: Int) =
        withContext(Dispatchers.IO) {
            safeResponse {
                api.getIncidentsForEvent(eventId)
            }
        }

    suspend fun getEventsBySportAndDate(sportSlug: String, date: LocalDate): Result<List<Event>> {
        val dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return safeResponse {
            api.getEventsBySportAndDate(sportSlug, dateString).onEach { event ->
                event.winnerCode = event.winnerCode ?: TeamSide.NONE
            }
        }
    }

    suspend fun getStandingsForTournament(tournamentId: Int) = safeResponse {
        api.getStandingsForTournament(tournamentId)
    }

    suspend fun getEventPage(tournamentId: Int, lastOrNext: LastOrNext, page: Int) = safeResponse {
        api.getEventPage(tournamentId, lastOrNext.toString().lowercase(), page)
    }

}

enum class LastOrNext {
    LAST,
    NEXT
}