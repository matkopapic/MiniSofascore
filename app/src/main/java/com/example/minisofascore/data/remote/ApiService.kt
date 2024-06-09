package com.example.minisofascore.data.remote

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Incident
import com.example.minisofascore.data.models.Player
import com.example.minisofascore.data.models.Team
import com.example.minisofascore.data.models.Tournament
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
    suspend fun getTournamentEventPage(@Path("id") tournamentId: Int, @Path("lastOrNext") lastOrNext: String, @Path("page") page: Int): List<Event>

    @GET("team/{teamId}")
    suspend fun getTeamDetails(@Path("teamId") teamId: Int): Team
    @GET("team/{teamId}/players")
    suspend fun getPlayers(@Path("teamId") teamId: Int): List<Player>
    @GET("team/{teamId}/events/{lastOrNext}/{page}")
    suspend fun getTeamEventPage(@Path("teamId") teamId: Int, @Path("lastOrNext") lastOrNext: String, @Path("page") page: Int): List<Event>
    @GET("team/{teamId}/tournaments")
    suspend fun getTeamTournaments(@Path("teamId") teamId: Int): List<Tournament>

    @GET("player/{playerId}/events/{lastOrNext}/{page}")
    suspend fun getPlayerEventPage(@Path("playerId") playerId: Int, @Path("lastOrNext") lastOrNext: String, @Path("page") page: Int): List<Event>

    @GET("sport/{slug}/tournaments")
    suspend fun getLeaguesBySport(@Path("slug") slug: String): List<Tournament>



}