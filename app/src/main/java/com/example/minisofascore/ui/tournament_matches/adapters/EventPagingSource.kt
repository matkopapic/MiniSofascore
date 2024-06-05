package com.example.minisofascore.ui.tournament_matches.adapters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.LastOrNext
import com.example.minisofascore.data.repository.Repository
import kotlin.math.abs

class EventPagingSource(
    private val tournamentId: Int,
    private val repository: Repository
) : PagingSource<Int, Event>() {
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

    private suspend fun getEventPage(page: Int): List<Event> {
        // page >= 0 upcoming matches
        // page < 0 previous matches
        val lastOrNext = if (page < 0) LastOrNext.LAST else LastOrNext.NEXT

        // when page == 0 that is /next/0
        // when page == -1 that is /last/0
        val pageNum = if (page < 0) abs(page + 1) else page
        return when (val response = repository.getEventPage(tournamentId, lastOrNext, pageNum)) {
            is Result.Success -> response.data
            is Result.Error -> throw Exception(response.error)
        }
    }
}