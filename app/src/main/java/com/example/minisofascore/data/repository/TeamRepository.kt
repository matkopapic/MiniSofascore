package com.example.minisofascore.data.repository

import com.example.minisofascore.data.models.TeamDetails
import com.example.minisofascore.data.remote.BASE_URL
import com.example.minisofascore.data.remote.Network
import com.example.minisofascore.util.safeResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

object TeamRepository {

    private val api = Network.getInstance()
    fun getTeamLogoUrl(teamId: Int) = "${BASE_URL}team/$teamId/image"


    suspend fun getTeamEventPage(teamId: Int, lastOrNext: LastOrNext, page: Int) = safeResponse {
        api.getTeamEventPage(teamId, lastOrNext.toString().lowercase(), page)
    }

    suspend fun getTeamDetails(teamId: Int) = coroutineScope {
        safeResponse {
            val details = async { api.getTeamDetails(teamId) }
            val tournaments = async { api.getTeamTournaments(teamId) }
            val matches = async { api.getTeamEventPage(teamId, LastOrNext.NEXT.toString().lowercase(), 0) }
            val players = async { api.getPlayers(teamId) }

            TeamDetails(
                team = details.await(),
                tournaments = tournaments.await(),
                nextMatches = matches.await(),
                players = players.await()
            )
        }
    }

}