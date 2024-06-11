package com.example.minisofascore.data.models

import com.example.minisofascore.R
import java.io.Serializable

data class Sport (
    val id: Int,
    val name: String,
    val slug: String
): Serializable

enum class SportType(val sportName: String, val slug: String, val stringRes: Int, val drawableRes: Int) {
    FOOTBALL("Football","football", R.string.football, R.drawable.ic_football),
    BASKETBALL("Basketball","basketball", R.string.basketball, R.drawable.ic_basketball),
    AMERICAN_FOOTBALL("Am. Football","american-football", R.string.am_football, R.drawable.ic_american_football)
}

fun Sport.getSportType(): SportType {
    return SportType.entries.firstOrNull{ it.slug == slug } ?: SportType.FOOTBALL
}