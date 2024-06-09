package com.example.minisofascore.ui.team_details_squad.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.minisofascore.R
import com.example.minisofascore.data.models.Player
import com.example.minisofascore.databinding.DayInfoLayoutBinding
import com.example.minisofascore.databinding.PlayerItemLayoutBinding
import com.example.minisofascore.databinding.SectionDividerBinding
import com.example.minisofascore.ui.main_list.adapters.EventAdapter.SectionDividerViewHolder
import com.example.minisofascore.util.loadFlag
import com.example.minisofascore.util.loadPlayerImage

class PlayerAdapter(
    private val context: Context,
    private val onPlayerClick: (Player) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = listOf<PlayerListItem>()

    fun updateItems(newItems: List<Player>, coachName: String) {
        val newList = mutableListOf<PlayerListItem>()
        newList.add(PlayerListItem.HeaderItem(context.getString(R.string.coach)))

        // creates empty player object for coaching since we only have the name
        newList.add(PlayerListItem.PlayerInfoItem(Player(0, coachName, "", newItems[0].country, "")))

        newItems.forEachIndexed { index, player ->
            newList.add(when (index) {
                0 -> PlayerListItem.HeaderItem(context.getString(R.string.players))
                else -> PlayerListItem.SectionDivider
            })
            newList.add(PlayerListItem.PlayerInfoItem(player))
        }
        items = newList
        notifyDataSetChanged()

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PlayerViewHolder.TYPE -> PlayerViewHolder(PlayerItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), onPlayerClick)
            HeaderViewHolder.TYPE -> HeaderViewHolder(DayInfoLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            else -> SectionDividerViewHolder(SectionDividerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PlayerListItem.PlayerInfoItem -> PlayerViewHolder.TYPE
            is PlayerListItem.HeaderItem -> HeaderViewHolder.TYPE
            is PlayerListItem.SectionDivider -> SectionDividerViewHolder.TYPE
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is PlayerListItem.PlayerInfoItem -> (holder as PlayerViewHolder).bind(item.player)
            is PlayerListItem.HeaderItem -> (holder as HeaderViewHolder).bind(item.text)
            else -> {}
        }
    }

    class PlayerViewHolder(
        private val binding: PlayerItemLayoutBinding,
        private val onPlayerClick: (Player) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        companion object {
            const val TYPE = 0
        }

        fun bind(player: Player) {
            binding.root.setOnClickListener{
                onPlayerClick(player)
            }
            binding.playerImage.loadPlayerImage(player.id)
            binding.playerName.text = player.name
            binding.playerCountryName.text = player.country.name
            binding.playerCountryLogo.loadFlag(player.country.name)
        }
    }

    class HeaderViewHolder(
        private val binding: DayInfoLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        companion object {
            const val TYPE = 1
        }

        fun bind(text: String) {
            binding.dayInfoDate.text = text
        }

    }


}