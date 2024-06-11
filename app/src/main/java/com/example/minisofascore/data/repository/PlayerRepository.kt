package com.example.minisofascore.data.repository

import com.example.minisofascore.data.remote.BASE_URL
import com.example.minisofascore.data.remote.Network
import com.example.minisofascore.util.safeResponse

object PlayerRepository {

    private val api = Network.getInstance()

    fun getPlayerImageUrl(playerId: Int) = "${BASE_URL}player/$playerId/image"

    suspend fun getPlayerEventPage(playerId: Int, lastOrNext: LastOrNext, page: Int) = safeResponse {
        api.getPlayerEventPage(playerId, lastOrNext.toString().lowercase(), page)
    }
}