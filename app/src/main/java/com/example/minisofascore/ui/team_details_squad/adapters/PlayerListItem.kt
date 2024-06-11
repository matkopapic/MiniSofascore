package com.example.minisofascore.ui.team_details_squad.adapters

import com.example.minisofascore.data.models.Player

sealed class PlayerListItem {

    data class PlayerInfoItem(val player: Player) : PlayerListItem()

    data class HeaderItem(val text: String): PlayerListItem()
    data object SectionDivider : PlayerListItem()

}