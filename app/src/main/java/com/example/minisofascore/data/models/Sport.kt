package com.example.minisofascore.data.models

import java.io.Serializable

data class Sport (
    val id: Int,
    val name: String,
    val slug: String
): Serializable

enum class SportType(val sportName: String, val slug: String) {
    FOOTBALL("Football","football"),
    BASKETBALL("Basketball","basketball"),
    AMERICAN_FOOTBALL("Am. Football","american-football")
}