package com.example.minisofascore.ui.leagues.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.databinding.LeagueLayoutBinding
import com.example.minisofascore.util.loadTournamentLogo

class LeaguesAdapter(
    private val onTournamentClick: (Tournament) -> Unit
) : RecyclerView.Adapter<LeaguesAdapter.LeagueViewHolder>() {

    private var items = listOf<Tournament>()

    fun updateItems(newItems: List<Tournament>) {
        val diffResult = DiffUtil.calculateDiff(LeaguesDiffCallback(items, newItems))
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    class LeagueViewHolder(
        private val binding: LeagueLayoutBinding,
        private val onTournamentClick: (Tournament) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(tournament: Tournament) {
            binding.run {
                logo.loadTournamentLogo(tournament.id)
                name.text = tournament.name
                root.setOnClickListener {
                    onTournamentClick(tournament)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        return LeagueViewHolder(LeagueLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), onTournamentClick)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class LeaguesDiffCallback(
    private val oldList: List<Tournament>,
    private val newList: List<Tournament>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }

}