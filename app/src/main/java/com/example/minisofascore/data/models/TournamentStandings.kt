package com.example.minisofascore.data.models

import com.google.gson.annotations.SerializedName

data class TournamentStandings(
    val id: Int,
    val tournament: Tournament,
    val type: StandingsType,
    val sortedStandingsRows: List<StandingsRow>
)

enum class StandingsType {
    @SerializedName("home")
    HOME,
    @SerializedName("away")
    AWAY,
    @SerializedName("total")
    TOTAL
}

data class StandingsRow (
    val id: Int,
    val team: Team,
    val points: Int?,
    val scoresFor: Int,
    val scoresAgainst: Int,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val percentage: Double?
)