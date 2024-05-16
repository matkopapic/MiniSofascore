package com.example.minisofascore.data.remote

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Sport
import retrofit2.http.GET

interface ApiService {

    @GET("sports")
    suspend fun getAllSports(): List<Sport>
    @GET("sport/football/events/2024-05-11")
    suspend fun getEventsByDate(): List<Event>


}