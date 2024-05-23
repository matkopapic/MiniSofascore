package com.example.minisofascore.data.remote

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Incident
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("sport/{sportSlug}/events/{dateString}")
    suspend fun getEventsBySportAndDate(@Path("sportSlug") sportSlug: String, @Path("dateString") dateString: String): List<Event>

    @GET("event/{eventId}/incidents")
    suspend fun getIncidentsForEvent(@Path("eventId") eventId: Int): List<Incident>

}