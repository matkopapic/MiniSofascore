package com.example.minisofascore.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

    suspend fun getTeamLogoById() =
        withContext(Dispatchers.IO) {
            val response = api.getTeamLogoById().execute()
            var bitmap: Bitmap? = null
            if (response.isSuccessful) {
                bitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
            }
            bitmap
        }
}