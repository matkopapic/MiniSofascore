package com.example.minisofascore.data.models

import java.io.Serializable

data class Sport (
    val id: Int,
    val name: String,
    val slug: String
): Serializable

enum class SportType {
    FOOTBALL,
    BASKETBALL,
    AMERICAN_FOOTBALL
}