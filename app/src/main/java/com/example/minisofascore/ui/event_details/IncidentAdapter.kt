package com.example.minisofascore.ui.event_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.minisofascore.R
import com.example.minisofascore.data.models.CardColor
import com.example.minisofascore.data.models.GoalType
import com.example.minisofascore.data.models.Incident
import com.example.minisofascore.data.models.TeamSide
import com.example.minisofascore.databinding.IncidentCardLayoutBinding
import com.example.minisofascore.databinding.IncidentGoalLayoutBinding
import com.example.minisofascore.databinding.IncidentPeriodLayoutBinding

class IncidentAdapter(private val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    private var items = listOf<Incident>()

    fun updateItems(newItems: List<Incident>) {
        val diffResult = DiffUtil.calculateDiff(IncidentDiffCallback(items, newItems))
        items = newItems.sortedWith(compareByDescending<Incident> { it.time }.thenByDescending { it.id })
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            0 -> CardIncidentViewHolder(IncidentCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), context)
            1 -> GoalIncidentViewHolder(IncidentGoalLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            2 -> PeriodIncidentViewHolder(IncidentPeriodLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is Incident.Card -> (holder as CardIncidentViewHolder).bind(item)
            is Incident.Goal -> (holder as GoalIncidentViewHolder).bind(item)
            is Incident.Period -> (holder as PeriodIncidentViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is Incident.Card -> 0
            is Incident.Goal -> 1
            is Incident.Period -> 2
        }
    }

    class CardIncidentViewHolder(
        private val binding: IncidentCardLayoutBinding,
        private val context: Context
    ): ViewHolder(binding.root) {

        fun bind(incident: Incident.Card) {
            when (incident.teamSide) {
                TeamSide.HOME -> binding.root.layoutDirection = View.LAYOUT_DIRECTION_LTR
                TeamSide.AWAY -> binding.root.layoutDirection = View.LAYOUT_DIRECTION_RTL
                else -> {}
            }
            val time = "${incident.time}'"
            binding.time.text = time
            binding.incidentIcon.setImageResource(incident.getDrawable())
            binding.playerName.text = incident.player.name
            binding.incidentType.text = context.getString(R.string.foul)
        }
    }

    class GoalIncidentViewHolder(
        private val binding: IncidentGoalLayoutBinding
    ): ViewHolder(binding.root) {
        fun bind(incident: Incident.Goal) {
             when (incident.scoringTeam) {
                TeamSide.HOME -> binding.root.layoutDirection = View.LAYOUT_DIRECTION_LTR
                TeamSide.AWAY -> binding.root.layoutDirection = View.LAYOUT_DIRECTION_RTL
                else -> {}
            }
            val time = "${incident.time}'"
            binding.time.text = time
            binding.incidentIcon.setImageResource(incident.getDrawable())
            binding.homeTeamScore.text = incident.homeScore.toString()
            binding.awayTeamScore.text = incident.awayScore.toString()
            binding.playerName.text = incident.player.name
        }
    }

    class PeriodIncidentViewHolder(
        private val binding: IncidentPeriodLayoutBinding
    ): ViewHolder(binding.root) {
        fun bind(incident: Incident.Period) {
            binding.periodText.text = incident.text
        }
    }
}

class IncidentDiffCallback(
    private val oldList: List<Incident>,
    private val newList: List<Incident>
): DiffUtil.Callback() {
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

fun Incident.Card.getDrawable() = when (this.color) {
    CardColor.RED -> R.drawable.ic_card_red
    CardColor.YELLOW_RED -> R.drawable.ic_card_red
    CardColor.YELLOW -> R.drawable.ic_card_yellow
}

fun Incident.Goal.getDrawable() = when (this.goalType) {
    GoalType.REGULAR -> R.drawable.ic_goal_regular
    GoalType.OWN_GOAL -> R.drawable.ic_own_goal
    GoalType.PENALTY -> R.drawable.ic_penalty_score
    GoalType.THREE_POINT -> R.drawable.ic_three
    GoalType.TWO_POINT -> R.drawable.ic_two
    GoalType.ONE_POINT -> R.drawable.ic_one
    GoalType.EXTRA_POINT -> R.drawable.ic_two_point_conversion
    GoalType.TOUCHDOWN -> R.drawable.ic_touchdown
    GoalType.FIELD_GOAL -> R.drawable.ic_field_goal
    GoalType.SAFETY -> R.drawable.ic_rogue
}