package com.example.minisofascore.data.repository

import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.SportType
import com.example.minisofascore.data.models.TeamSide
import com.example.minisofascore.data.remote.Network
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.util.safeResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object EventRepository {
    private val api = Network.getInstance()


    suspend fun getIncidentsForEvent(eventId: Int) =
        safeResponse {
            api.getIncidentsForEvent(eventId)
        }

    suspend fun getEventsBySportAndDate(sportSlug: String, date: LocalDate): Result<List<Event>> {
        val dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return safeResponse {
            api.getEventsBySportAndDate(sportSlug, dateString).onEach { event ->
                event.winnerCode = event.winnerCode ?: TeamSide.NONE
            }
        }
    }

    suspend fun getLeaguesBySport(sportType: SportType) = safeResponse {
        api.getLeaguesBySport(sportType.slug)
    }

    suspend fun getEvent(eventId: Int) = safeResponse {
        api.getEvent(eventId)
    }


}

enum class LastOrNext {
    LAST,
    NEXT
}