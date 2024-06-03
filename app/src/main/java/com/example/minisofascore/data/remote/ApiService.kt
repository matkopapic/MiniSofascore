package com.example.minisofascore.data.remote

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Incident
import com.example.minisofascore.data.models.TournamentStandings
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("sport/{sportSlug}/events/{dateString}")
    suspend fun getEventsBySportAndDate(@Path("sportSlug") sportSlug: String, @Path("dateString") dateString: String): List<Event>

    @GET("event/{eventId}/incidents")
    suspend fun getIncidentsForEvent(@Path("eventId") eventId: Int): List<Incident>

    @GET("event/{eventId}")
    suspend fun getEvent(@Path("eventId") eventId: Int): Event

    @GET("tournament/{id}/standings")
    suspend fun getStandingsForTournament(@Path("id") tournamentId: Int): List<TournamentStandings>
    @GET("tournament/{id}/events/{lastOrNext}/{page}")
    suspend fun getEventPage(@Path("id") tournamentId: Int, @Path("lastOrNext") lastOrNext: String, @Path("page") page: Int): List<Event>



}