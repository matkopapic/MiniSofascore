package com.example.minisofascore.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.minisofascore.data.remote.Network
import com.example.minisofascore.util.safeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Repository {
    private val api = Network.getInstance()

    suspend fun getAllSports() =
        withContext(Dispatchers.IO) {
            safeResponse {
                api.getAllSports()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getEventsByDate(date: LocalDate) =
        withContext(Dispatchers.IO) {
            val dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            safeResponse {
                api.getEventsByDate(dateString)
            }
        }

    suspend fun getTeamLogoById(teamId: Int) =
        withContext(Dispatchers.IO) {
            val response = api.getTeamLogoById(teamId).execute()
            var bitmap: Bitmap? = null
            if (response.isSuccessful) {
                bitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
            }
            bitmap
        }

    suspend fun getTournamentLogoById(tournamentId: Int) =
        withContext(Dispatchers.IO) {
            val response = api.getTournamentLogoById(tournamentId).execute()
            var bitmap: Bitmap? = null
            if (response.isSuccessful) {
                bitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
            }
            bitmap
        }
}