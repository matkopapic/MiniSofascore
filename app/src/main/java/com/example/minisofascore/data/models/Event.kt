package com.example.minisofascore.data.models

import android.graphics.Bitmap
import java.sql.Timestamp


data class Event (
    val id: Int,
    val slug: String,
    val tournament: Tournament,
    val homeTeam: Team,
    val awayTeam: Team,
    val status: String,
    val startDate: Timestamp,
    val homeScore: Score,
    val awayScore: Score,
    val winnerCode: String,
    val round: Int
)

data class Tournament(
    val id: Int,
    val name: String,
    val slug: String,
    val sport: Sport,
    val country: Country,
    var logo: Bitmap? = null
)

data class Team(
    val id: Int,
    val name: String,
    val country: Country,
    var logo: Bitmap? = null
)

data class Score(
    val total: Int,
    val period1: Int,
    val period2: Int,
    val period3: Int,
    val period4: Int,
    val overtime: Int
)

data class Country(
    val id: Int,
    val name: String
)