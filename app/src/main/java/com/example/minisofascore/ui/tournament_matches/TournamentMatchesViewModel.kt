package com.example.minisofascore.ui.tournament_matches

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.minisofascore.data.repository.Repository
import com.example.minisofascore.ui.tournament_matches.adapters.EventPagingSource

class TournamentMatchesViewModel : ViewModel() {
    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    private val repository = Repository()


    fun getEventPageLiveData(tournamentId: Int) =
        Pager(
            PagingConfig(NETWORK_PAGE_SIZE)
        ) {
            EventPagingSource(tournamentId, repository)
        }.liveData

}