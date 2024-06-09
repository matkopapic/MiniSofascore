package com.example.minisofascore.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

sealed class Incident(
    val id: Int,
    val time: Int,
    val type: IncidentType
): Serializable {
    class Card(
        id: Int,
        time: Int,
        type: IncidentType,
        val player: Player,
        val teamSide: TeamSide,
        val color: CardColor,
    ): Incident(id, time, type)

    class Goal(
        id: Int,
        time: Int,
        type: IncidentType,
        val player: Player,
        val homeScore: Int,
        val awayScore: Int,
        val goalType: GoalType,
        val scoringTeam: TeamSide
    ): Incident(id, time, type)

    class Period(
        id: Int,
        time: Int,
        type: IncidentType,
        val text: String,
        var isColoredLive: Boolean = false
    ): Incident(id, time, type)
}



enum class IncidentType {
    @SerializedName("card")
    CARD,
    @SerializedName("goal")
    GOAL,
    @SerializedName("period")
    PERIOD
}

enum class CardColor {
    @SerializedName("yellow")
    YELLOW,
    @SerializedName("yellowred")
    YELLOW_RED,
    @SerializedName("red")
    RED
}

data class Player(
    val id: Int,
    val name: String,
    val slug: String,
    val country: Country,
    val position: String
): Serializable

enum class GoalType{
    @SerializedName("regular") REGULAR,
    @SerializedName("owngoal") OWN_GOAL,
    @SerializedName("penalty") PENALTY,
    @SerializedName("onepoint") ONE_POINT,
    @SerializedName("twopoint") TWO_POINT,
    @SerializedName("threepoint") THREE_POINT,
    @SerializedName("touchdown") TOUCHDOWN,
    @SerializedName("safety") SAFETY,
    @SerializedName("fieldgoal") FIELD_GOAL,
    @SerializedName("extrapoint") EXTRA_POINT
}
