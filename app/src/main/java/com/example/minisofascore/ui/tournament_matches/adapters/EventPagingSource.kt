package com.example.minisofascore.ui.tournament_matches.adapters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.minisofascore.data.models.Event

abstract class EventPagingSource : PagingSource<Int, Event>() {
    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        val position = params.key ?: 0
        return try {
            val data = getEventPage(position)
            LoadResult.Page(
                data = data,
                prevKey = (position - 1).takeIf { data.isNotEmpty() },
                nextKey = (position + 1).takeIf { data.isNotEmpty() }
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    abstract suspend fun getEventPage(page: Int): List<Event>


}