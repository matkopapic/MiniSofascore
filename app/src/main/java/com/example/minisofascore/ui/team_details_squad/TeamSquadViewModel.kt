package com.example.minisofascore.ui.team_details_squad

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.minisofascore.R
import com.example.minisofascore.data.models.Player
import com.example.minisofascore.ui.team_details_squad.adapters.PlayerAdapter
import com.example.minisofascore.ui.team_details_squad.adapters.PlayerListItem

class TeamSquadViewModel(private val application: Application) : AndroidViewModel(application) {

    fun updateAdapterWithList(newItems: List<Player>, coachName: String, adapter: PlayerAdapter) {
        val newList = mutableListOf<PlayerListItem>()
        newList.add(PlayerListItem.HeaderItem(application.getString(R.string.coach)))

        // creates empty player object for coaching since we only have the name
        newList.add(PlayerListItem.PlayerInfoItem(Player(0, coachName, "", newItems[0].country, "")))

        newItems.forEachIndexed { index, player ->
            newList.add(when (index) {
                0 -> PlayerListItem.HeaderItem(application.getString(R.string.players))
                else -> PlayerListItem.SectionDivider
            })
            newList.add(PlayerListItem.PlayerInfoItem(player))
        }

        adapter.updateItems(newList)
    }
}