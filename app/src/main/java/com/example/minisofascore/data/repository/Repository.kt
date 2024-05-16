package com.example.minisofascore.data.repository

import com.example.minisofascore.data.remote.Network
import com.example.minisofascore.util.safeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {
    private val api = Network.getInstance()

    suspend fun getAllSports() =
        withContext(Dispatchers.IO) {
            safeResponse {
                api.getAllSports()
            }
        }

    suspend fun getEventsByDate() =
        withContext(Dispatchers.IO) {
            safeResponse {
                api.getEventsByDate()
            }
        }
}