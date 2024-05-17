package com.example.minisofascore.data.remote

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Sport
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("sports")
    suspend fun getAllSports(): List<Sport>

    @GET("sport/football/events/2024-05-11")
    suspend fun getEventsByDate(): List<Event>

    @GET("team/{id}/image")
    fun getTeamLogoById(@Path("id") teamId: Int): Call<ResponseBody>


}