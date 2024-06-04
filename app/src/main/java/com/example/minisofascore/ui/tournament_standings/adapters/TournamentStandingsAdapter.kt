package com.example.minisofascore.ui.tournament_standings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.minisofascore.data.models.SportType
import com.example.minisofascore.data.models.StandingsRow
import com.example.minisofascore.data.models.TournamentStandings
import com.example.minisofascore.databinding.AmFootballStandingsHeaderLayoutBinding
import com.example.minisofascore.databinding.AmFootballStandingsItemLayoutBinding
import com.example.minisofascore.databinding.BasketballStadingsHeaderLayoutBinding
import com.example.minisofascore.databinding.BasketballStandingsItemLayoutBinding
import com.example.minisofascore.databinding.FootballStandingsHeaderLayoutBinding
import com.example.minisofascore.databinding.FootballStandingsItemLayoutBinding
import java.util.Locale

class TournamentStandingsAdapter(private val standings: TournamentStandings, private val sportType: SportType) : RecyclerView.Adapter<ViewHolder>(){

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> when(sportType) {
                SportType.FOOTBALL -> FootballHeaderViewHolder.create(parent)
                SportType.BASKETBALL -> BasketballHeaderViewHolder.create(parent)
                SportType.AMERICAN_FOOTBALL -> AmericanFootballHeaderViewHolder.create(parent)
            }
            else -> when(sportType) {
                SportType.FOOTBALL -> FootballItemViewHolder.create(parent,standings)
                SportType.BASKETBALL -> BasketballItemViewHolder.create(parent,standings)
                SportType.AMERICAN_FOOTBALL -> AmericanFootballItemViewHolder.create(parent,standings)
            }
        }
    }

    override fun getItemCount() = standings.sortedStandingsRows.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> when (sportType) {
                SportType.FOOTBALL -> (holder as FootballHeaderViewHolder).bind()
                SportType.BASKETBALL -> (holder as BasketballHeaderViewHolder).bind()
                SportType.AMERICAN_FOOTBALL -> (holder as AmericanFootballHeaderViewHolder).bind()
            }
            else -> when (sportType) {
                SportType.FOOTBALL -> (holder as FootballItemViewHolder).bind(standings.sortedStandingsRows[position-1])
                SportType.BASKETBALL -> (holder as BasketballItemViewHolder).bind(standings.sortedStandingsRows[position-1])
                SportType.AMERICAN_FOOTBALL -> (holder as AmericanFootballItemViewHolder).bind(standings.sortedStandingsRows[position-1])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_ITEM
        }
    }

    class FootballHeaderViewHolder (
        binding: FootballStandingsHeaderLayoutBinding
    ): ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup) = FootballHeaderViewHolder(FootballStandingsHeaderLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
        }
        fun bind() {}
    }

    class FootballItemViewHolder (
        private val binding: FootballStandingsItemLayoutBinding,
        private val standings: TournamentStandings
    ): ViewHolder(binding.root) {
        companion object {
            fun create(parent:ViewGroup, standings: TournamentStandings) = FootballItemViewHolder(FootballStandingsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false), standings)
        }
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

    class BasketballHeaderViewHolder(
        binding: BasketballStadingsHeaderLayoutBinding
    ): ViewHolder(binding.root) {
        companion object {
            fun create(parent:ViewGroup) = BasketballHeaderViewHolder(
                BasketballStadingsHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        fun bind(){}
    }

    class BasketballItemViewHolder(
        private val binding: BasketballStandingsItemLayoutBinding,
        private val standings: TournamentStandings
    ): ViewHolder(binding.root) {

        companion object {
            fun create(parent:ViewGroup, standings: TournamentStandings) = BasketballItemViewHolder(
                BasketballStandingsItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false), standings)
        }

        private val gamesDiffLeader = standings.sortedStandingsRows[0].wins - standings.sortedStandingsRows[0].losses

        fun bind(row: StandingsRow) {
            val position = standings.sortedStandingsRows.indexOf(row) + 1
            binding.position.text = position.toString()
            binding.name.text = row.team.name
            binding.played.text = row.played.toString()
            binding.wins.text = row.wins.toString()
            binding.losses.text = row.losses.toString()
            val goalDiff = row.scoresFor - row.scoresAgainst
            binding.diff.text = goalDiff.toString()
            val streak = row.wins - row.losses
            binding.streak.text = streak.toString()
            val gamesBehind = gamesDiffLeader - (row.wins - row.losses)
            if (position != 1) {
                binding.gamesBehind.text = (gamesBehind / 2f).toString()
            } else {
                binding.gamesBehind.text = "-"
            }
            val percentage = row.wins.toFloat() / (row.wins + row.losses)
            binding.percentage.text = String.format(Locale.getDefault(),"%.3f", percentage)
        }
    }

    class AmericanFootballHeaderViewHolder(
        binding: AmFootballStandingsHeaderLayoutBinding
    ): ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup) = AmericanFootballHeaderViewHolder(
                AmFootballStandingsHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        fun bind(){}
    }

    class AmericanFootballItemViewHolder(
        private val binding: AmFootballStandingsItemLayoutBinding,
        private val standings: TournamentStandings
    ): ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup, standings: TournamentStandings) = AmericanFootballItemViewHolder(
                AmFootballStandingsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), standings)
        }
        fun bind(row: StandingsRow) {
            val position = standings.sortedStandingsRows.indexOf(row) + 1
            binding.position.text = position.toString()
            binding.name.text = row.team.name
            binding.played.text = row.played.toString()
            binding.wins.text = row.wins.toString()
            binding.draws.text = row.draws.toString()
            binding.losses.text = row.losses.toString()
            val percentage = row.wins.toFloat() / (row.wins + row.losses)
            binding.percentage.text = String.format(Locale.getDefault(),"%.3f", percentage)
        }
    }
}