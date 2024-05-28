package com.example.minisofascore.ui.event_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.minisofascore.R
import com.example.minisofascore.data.models.CardColor
import com.example.minisofascore.data.models.GoalType
import com.example.minisofascore.data.models.Incident
import com.example.minisofascore.data.models.SportType
import com.example.minisofascore.data.models.TeamSide
import com.example.minisofascore.databinding.IncidentBbPointLayoutBinding
import com.example.minisofascore.databinding.IncidentCardLayoutBinding
import com.example.minisofascore.databinding.IncidentGoalLayoutBinding
import com.example.minisofascore.databinding.IncidentPeriodLayoutBinding
import com.google.android.material.color.MaterialColors

class IncidentAdapter(private val context: Context, private val sportType: SportType, private val isLive: Boolean) : RecyclerView.Adapter<ViewHolder>() {

    private var items = listOf<Incident>()

    fun updateItems(newItems: List<Incident>) {
        val diffResult = DiffUtil.calculateDiff(IncidentDiffCallback(items, newItems))
        items = newItems.sortedWith(compareByDescending<Incident> { it.time }.thenByDescending { it.id })
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            CardIncidentViewHolder.TYPE -> CardIncidentViewHolder(IncidentCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), context)
            GoalIncidentViewHolder.TYPE -> {
                when (sportType) {
                    SportType.FOOTBALL -> GoalIncidentViewHolder(IncidentGoalLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                    SportType.BASKETBALL -> BasketballPointViewHolder(IncidentBbPointLayoutBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false))
                    SportType.AMERICAN_FOOTBALL -> GoalIncidentViewHolder(IncidentGoalLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                }
            }
            PeriodIncidentViewHolder.TYPE -> PeriodIncidentViewHolder(IncidentPeriodLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is Incident.Card -> (holder as CardIncidentViewHolder).bind(item)
            is Incident.Goal -> {
                when (sportType) {
                    SportType.FOOTBALL -> (holder as GoalIncidentViewHolder).bind(item)
                    SportType.BASKETBALL -> (holder as BasketballPointViewHolder).bind(item, position == items.size-1)
                    SportType.AMERICAN_FOOTBALL -> (holder as GoalIncidentViewHolder).bind(item)
                }
            }
            is Incident.Period -> (holder as PeriodIncidentViewHolder).bind(item, position == 0 && isLive)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is Incident.Card -> CardIncidentViewHolder.TYPE
            is Incident.Goal -> GoalIncidentViewHolder.TYPE
            is Incident.Period -> PeriodIncidentViewHolder.TYPE
        }
    }

    class CardIncidentViewHolder(
        private val binding: IncidentCardLayoutBinding,
        private val context: Context
    ): ViewHolder(binding.root) {

        companion object {
            const val TYPE = 0
        }

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

        companion object {
            const val TYPE = 1
        }
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

        companion object {
            const val TYPE = 2
        }

        private val liveColor = MaterialColors.getColor(binding.root, R.attr.live)
        fun bind(incident: Incident.Period, isLiveColor: Boolean) {
            binding.periodText.text = incident.text
            if (isLiveColor) {
                binding.periodText.setTextColor(liveColor)
            }
        }
    }

    class BasketballPointViewHolder(
        private val binding: IncidentBbPointLayoutBinding
    ): ViewHolder(binding.root) {

        fun bind(incident: Incident.Goal, isLast: Boolean) {
            when (incident.scoringTeam) {
                TeamSide.HOME -> {
                    binding.incidentInfoLayout.layoutDirection = View.LAYOUT_DIRECTION_LTR
                    binding.incidentInfoLayout.alignParentStart()
                }
                TeamSide.AWAY -> {
                    binding.incidentInfoLayout.layoutDirection = View.LAYOUT_DIRECTION_RTL
                    binding.incidentInfoLayout.alignParentEnd()
                }
                TeamSide.NONE -> {}
            }
            val time = "${incident.time}'"
            binding.time.text = time
            binding.incidentIcon.setImageResource(incident.getDrawable())
            binding.homeTeamScore.text = incident.homeScore.toString()
            binding.awayTeamScore.text = incident.awayScore.toString()
            if (isLast) {
                binding.dividerTime.visibility = View.GONE
            } else {
                binding.dividerTime.visibility = View.VISIBLE
            }
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

fun View.alignParentStart() {
    val layoutParams = layoutParams as RelativeLayout.LayoutParams
    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
    setLayoutParams(layoutParams)
}

fun View.alignParentEnd() {
    val layoutParams = layoutParams as RelativeLayout.LayoutParams
    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
    setLayoutParams(layoutParams)
}