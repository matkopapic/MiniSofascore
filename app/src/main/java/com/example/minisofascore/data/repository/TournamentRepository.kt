package com.example.minisofascore.data.repository

import com.example.minisofascore.data.remote.BASE_URL
import com.example.minisofascore.data.remote.Network
import com.example.minisofascore.util.safeResponse

object TournamentRepository {

    private val api = Network.getInstance()
    fun getTournamentLogoUrl(tournamentId: Int) = "${BASE_URL}tournament/$tournamentId/image"

    suspend fun getStandingsForTournament(tournamentId: Int) = safeResponse {
        api.getStandingsForTournament(tournamentId)
    }

    suspend fun getTournamentEventPage(tournamentId: Int, lastOrNext: LastOrNext, page: Int) = safeResponse {
        api.getTournamentEventPage(tournamentId, lastOrNext.toString().lowercase(), page)
    }

}