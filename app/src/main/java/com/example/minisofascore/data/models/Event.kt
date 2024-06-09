package com.example.minisofascore.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.sql.Timestamp


data class Event (
    val id: Int,
    val slug: String,
    val tournament: Tournament,
    val homeTeam: Team,
    val awayTeam: Team,
    val status: EventStatus,
    val startDate: Timestamp,
    val homeScore: Score,
    val awayScore: Score,
    var winnerCode: TeamSide?,
    val round: Int
): Serializable

enum class EventStatus {
    @SerializedName("notstarted")
    NOT_STARTED,
    @SerializedName("inprogress")
    IN_PROGRESS,
    @SerializedName("finished")
    FINISHED
}

enum class TeamSide {
    @SerializedName("home")
    HOME,
    @SerializedName("away")
    AWAY,
    @SerializedName("null")
    NONE
}

data class Tournament(
    val id: Int,
    val name: String,
    val slug: String,
    val sport: Sport,
    val country: Country,
): Serializable

data class Score(
    val total: Int,
    val period1: Int,
    val period2: Int,
    val period3: Int,
    val period4: Int,
    val overtime: Int
): Serializable

data class Country(
    val id: Int,
    val name: String
): Serializable