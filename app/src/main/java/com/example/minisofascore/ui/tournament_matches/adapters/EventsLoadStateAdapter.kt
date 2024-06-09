package com.example.minisofascore.ui.tournament_matches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minisofascore.R
import com.example.minisofascore.databinding.EventsLoadStateItemBinding

class EventsLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<EventsLoadStateAdapter.EventLoadStateViewHolder>()  {

    override fun onBindViewHolder(holder: EventLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): EventLoadStateViewHolder {
        return EventLoadStateViewHolder.create(parent, retry)
    }

    class EventLoadStateViewHolder(
        private val binding: EventsLoadStateItemBinding,
        private val context: Context,
        retry: () -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): EventLoadStateViewHolder {
                val binding = EventsLoadStateItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return EventLoadStateViewHolder(binding,parent.context, retry)
            }
        }
        init {
            binding.retryButton.setOnClickListener{
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = context.getString(R.string.notLoading)
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }


    }


}