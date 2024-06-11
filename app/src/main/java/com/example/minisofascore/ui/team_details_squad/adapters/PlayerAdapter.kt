package com.example.minisofascore.ui.team_details_squad.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.minisofascore.R
import com.example.minisofascore.data.models.Player
import com.example.minisofascore.databinding.DayInfoLayoutBinding
import com.example.minisofascore.databinding.PlayerItemLayoutBinding
import com.example.minisofascore.databinding.SectionDividerBinding
import com.example.minisofascore.ui.main_list.adapters.EventAdapter.SectionDividerViewHolder
import com.example.minisofascore.util.loadFlag
import com.example.minisofascore.util.loadPlayerImage
import com.google.android.material.color.MaterialColors

class PlayerAdapter(
    private val onPlayerClick: (Player) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = listOf<PlayerListItem>()

    fun updateItems(newItems: List<PlayerListItem>) {

        val result = DiffUtil.calculateDiff(PlayerDiffCallback(items, newItems))
        items = newItems
        result.dispatchUpdatesTo(this)

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
            binding.root.setBackgroundColor(MaterialColors.getColor(binding.root, R.attr.surface_1))
        }

    }


}

class PlayerDiffCallback(
    private val oldList: List<PlayerListItem>,
    private val newList: List<PlayerListItem>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }

}