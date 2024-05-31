package com.example.minisofascore.ui.tournament_matches.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.databinding.EventItemLayoutBinding
import com.example.minisofascore.ui.main_list.adapters.EventAdapter

class EventPagingAdapter : PagingDataAdapter<Event, EventAdapter.EventInfoViewHolder>(EventComparator) {


    override fun onBindViewHolder(holder: EventAdapter.EventInfoViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventAdapter.EventInfoViewHolder {
        return EventAdapter.EventInfoViewHolder(
            EventItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            parent.context,
        ) {}
    }

    object EventComparator : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

    }

}