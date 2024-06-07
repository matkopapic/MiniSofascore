package com.example.minisofascore.data.models

import java.io.Serializable

data class Team(
    val id: Int,
    val name: String,
    val country: Country,
    val managerName: String?,
    val venue: String?
): Serializable

data class TeamDetails(
    val team: Team,
    val tournaments: List<Tournament>,
    val nextMatches: List<Event>,
    val players: List<Player>
): Serializable