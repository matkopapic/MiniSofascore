package com.example.minisofascore.data.repository

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.TeamDetails
import com.example.minisofascore.data.models.TeamSide
import com.example.minisofascore.data.remote.BASE_URL
import com.example.minisofascore.data.remote.Network
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.util.safeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Repository {
    private val api = Network.getInstance()

    companion object {
        fun getTeamLogoUrl(teamId: Int) = "${BASE_URL}team/$teamId/image"
        fun getTournamentLogoUrl(tournamentId: Int) = "${BASE_URL}tournament/$tournamentId/image"

        fun getPlayerImageUrl(playerId: Int) = "${BASE_URL}player/$playerId/image"
        fun getFlagUrl(countryCode: String) = "https://flagsapi.com/${countryCode}/flat/64.png"
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

    suspend fun getTournamentEventPage(tournamentId: Int, lastOrNext: LastOrNext, page: Int) = safeResponse {
        api.getTournamentEventPage(tournamentId, lastOrNext.toString().lowercase(), page)
    }

    suspend fun getTeamEventPage(teamId: Int, lastOrNext: LastOrNext, page: Int) = safeResponse {
        api.getTeamEventPage(teamId, lastOrNext.toString().lowercase(), page)
    }

    suspend fun getEvent(eventId: Int) = safeResponse {
        api.getEvent(eventId)
    }

    suspend fun getTeamDetails(teamId: Int) = safeResponse {

        val details = CoroutineScope(Dispatchers.IO).async { api.getTeamDetails(teamId) }
        val tournaments = CoroutineScope(Dispatchers.IO).async { api.getTeamTournaments(teamId) }
        val matches = CoroutineScope(Dispatchers.IO).async { api.getTeamEventPage(teamId, LastOrNext.NEXT.toString().lowercase(), 0) }
        val players = CoroutineScope(Dispatchers.IO).async { api.getPlayers(teamId) }

        TeamDetails(
            team = details.await(),
            tournaments = tournaments.await(),
            nextMatches = matches.await(),
            players = players.await()
        )

    }

}

enum class LastOrNext {
    LAST,
    NEXT
}