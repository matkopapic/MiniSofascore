package com.example.minisofascore.ui.tournament_standings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.minisofascore.data.models.StandingsRow
import com.example.minisofascore.data.models.TournamentStandings
import com.example.minisofascore.databinding.FootballStandingsHeaderLayoutBinding
import com.example.minisofascore.databinding.FootballStandingsItemLayoutBinding

class TournamentStandingsAdapter(private val standings: TournamentStandings) : RecyclerView.Adapter<ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> FootballHeaderViewHolder(FootballStandingsHeaderLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            else -> FootballItemViewHolder(FootballStandingsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false), standings)
        }
    }

    override fun getItemCount() = standings.sortedStandingsRows.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> (holder as FootballHeaderViewHolder).bind()
            else -> (holder as FootballItemViewHolder).bind(standings.sortedStandingsRows[position-1])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0
            else -> 1
        }
    }

    class FootballHeaderViewHolder (
        binding: FootballStandingsHeaderLayoutBinding
    ): ViewHolder(binding.root) {
        fun bind() {

        }
    }

    class FootballItemViewHolder (
        private val binding: FootballStandingsItemLayoutBinding,
        private val standings: TournamentStandings
    ): ViewHolder(binding.root) {
        fun bind(row: StandingsRow) {
            val position = standings.sortedStandingsRows.indexOf(row) + 1
            binding.position.text = position.toString()
            binding.name.text = row.team.name
            binding.played.text = row.played.toString()
            binding.wins.text = row.wins.toString()
            binding.draws.text = row.draws.toString()
            binding.losses.text = row.losses.toString()
            val goalDiff = "${row.scoresFor}:${row.scoresAgainst}"
            binding.goalDiff.text = goalDiff
            binding.points.text = (row.points ?: 0).toString()
        }
    }
}