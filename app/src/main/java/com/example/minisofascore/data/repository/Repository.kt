package com.example.minisofascore.data.repository

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.remote.BASE_URL
import com.example.minisofascore.data.remote.Network
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.util.safeResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Repository {
    private val api = Network.getInstance()

    companion object {
        fun getTeamLogoUrl(teamId: Int) = "${BASE_URL}team/$teamId/image"
        fun getTournamentLogoUrl(tournamentId: Int) = "${BASE_URL}tournament/$tournamentId/image"
    }

    suspend fun getEventsBySportAndDate(sportSlug: String, date: LocalDate): Result<List<Event>> {
        val dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return safeResponse {
            api.getEventsBySportAndDate(sportSlug, dateString).onEach { event ->
                event.winnerCode = event.winnerCode ?: "unknown"
            }
        }

    }

}