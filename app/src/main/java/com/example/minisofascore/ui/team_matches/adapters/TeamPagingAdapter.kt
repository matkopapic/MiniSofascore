package com.example.minisofascore.ui.team_matches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minisofascore.R
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.databinding.EventItemLayoutBinding
import com.example.minisofascore.databinding.TournamentHeaderLayoutBinding
import com.example.minisofascore.ui.main_list.adapters.EventAdapter
import com.example.minisofascore.ui.main_list.adapters.EventListItem
import com.example.minisofascore.ui.tournament_matches.adapters.EventPagingAdapter
import com.example.minisofascore.ui.tournament_matches.adapters.StickyHeaderItemDecorator
import com.example.minisofascore.util.loadTournamentLogo

class TeamPagingAdapter(
    private val context: Context,
    private val onEventClick: (Event) -> Unit,
    private val onTournamentClick: (Tournament) -> Unit
) : PagingDataAdapter<EventListItem, RecyclerView.ViewHolder>(EventPagingAdapter.EventListItemComparator),
    StickyHeaderDecorator.StickyHeaderInterface
{

    override fun getItemViewType(position: Int): Int {
        return try {
            when(getItem(position)) {
                is EventListItem.EventItem -> EventAdapter.EventInfoViewHolder.TYPE
                is EventListItem.TournamentHeaderItem -> EventAdapter.TournamentHeaderViewHolder.TYPE
                else -> -1
            }
        } catch (exception: IndexOutOfBoundsException) {
            return -1
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is EventListItem.EventItem -> (holder as EventAdapter.EventInfoViewHolder).bind(item.event)
            is EventListItem.TournamentHeaderItem -> (holder as EventAdapter.TournamentHeaderViewHolder).bind(item.tournament)
            else -> {}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EventAdapter.EventInfoViewHolder.TYPE -> EventAdapter.EventInfoViewHolder(
                EventItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                context,
                onEventClick
            )
            EventAdapter.TournamentHeaderViewHolder.TYPE -> EventAdapter.TournamentHeaderViewHolder(
                TournamentHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onTournamentClick
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return getItemViewType(itemPosition) == EventAdapter.TournamentHeaderViewHolder.TYPE
    }

}