package com.example.minisofascore.ui.tournament_matches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.minisofascore.R
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.databinding.DayInfoLayoutBinding
import com.example.minisofascore.databinding.EventItemLayoutBinding
import com.example.minisofascore.ui.main_list.adapters.EventAdapter
import com.example.minisofascore.ui.main_list.adapters.EventListItem

class EventPagingAdapter(
    private val context: Context,
    private val onEventClick: (Event) -> Unit
) : PagingDataAdapter<EventListItem, ViewHolder>(EventListItemComparator),
 StickyHeaderItemDecorator.StickyHeaderInterface
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            EventAdapter.EventInfoViewHolder.TYPE -> EventAdapter.EventInfoViewHolder(
                EventItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                context,
                onEventClick
            )
            DayInfoViewHolder.TYPE -> DayInfoViewHolder(
                DayInfoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                context
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return try {
            when(getItem(position)) {
                is EventListItem.EventItem -> EventAdapter.EventInfoViewHolder.TYPE
                is EventListItem.DayInfoItem -> DayInfoViewHolder.TYPE
                else -> -1
            }
        } catch (exception: IndexOutOfBoundsException) {
            return -1
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is EventListItem.EventItem -> (holder as EventAdapter.EventInfoViewHolder).bind(item.event)
            is EventListItem.DayInfoItem -> (holder as DayInfoViewHolder).bind(item.round)
            else -> {}
        }
    }

    class DayInfoViewHolder(
        private val binding: DayInfoLayoutBinding,
        private val context: Context
    ): ViewHolder(binding.root) {

        companion object {
            const val TYPE = 3
        }
        fun bind(roundNum: Int) {
            val roundText = "${context.getString(R.string.round)} $roundNum"
            binding.dayInfoDate.text = roundText
            binding.dayInfoEvents.text = ""

        }
    }

    object EventListItemComparator : DiffUtil.ItemCallback<EventListItem>() {
        override fun areItemsTheSame(oldItem: EventListItem, newItem: EventListItem): Boolean {
            return when {
                oldItem is EventListItem.TournamentHeaderItem
                        && newItem is EventListItem.TournamentHeaderItem -> {
                    oldItem.tournament.id == newItem.tournament.id
                }
                oldItem is EventListItem.EventItem
                        && newItem is EventListItem.EventItem -> {
                    oldItem.event.id == newItem.event.id
                }
                oldItem::class == newItem::class -> true
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: EventListItem, newItem: EventListItem): Boolean {
            return when {
                oldItem is EventListItem.TournamentHeaderItem
                        && newItem is EventListItem.TournamentHeaderItem -> {
                    oldItem.tournament == newItem.tournament
                }
                oldItem is EventListItem.EventItem
                        && newItem is EventListItem.EventItem -> {
                    oldItem.event == newItem.event
                }
                oldItem is EventListItem.DayInfoItem
                        && newItem is EventListItem.DayInfoItem -> {
                    newItem.date == oldItem.date && newItem.numOfEvents == oldItem.numOfEvents
                }
                oldItem::class == newItem::class -> true
                else -> false
            }
        }

    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        return (itemPosition downTo 0).firstOrNull{ isHeader(it) } ?: 0
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.day_info_layout
    }

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        if (isHeader(headerPosition)) {
            val roundText = "${context.getString(R.string.round)} ${(getItem(headerPosition) as EventListItem.DayInfoItem).round}"
            (header as ConstraintLayout).findViewById<TextView>(R.id.day_info_date).text = roundText
        }

    }

    override fun isHeader(itemPosition: Int): Boolean {
        return getItemViewType(itemPosition) == DayInfoViewHolder.TYPE
    }

}