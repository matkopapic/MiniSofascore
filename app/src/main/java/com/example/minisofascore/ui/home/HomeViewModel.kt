package com.example.minisofascore.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repo = Repository()

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    init {
        viewModelScope.launch {
            when(val response = repo.getEventsByDate()) {
                is Result.Error -> Log.d("aaaa", "error:${response.error}")
                is Result.Success -> {
                    // list without logos is in response.data
                    // retrieve all logos in parallel
                    val deferredTeamLogos = response.data.map { event ->
                        async {
                            val imageBitmapHome = repo.getTeamLogoById(event.homeTeam.id)
                            val imageBitmapAway = repo.getTeamLogoById(event.awayTeam.id)
                            if (imageBitmapHome != null)
                                event.homeTeam.logo = imageBitmapHome
                            if (imageBitmapAway != null)
                                event.awayTeam.logo = imageBitmapAway
                        }
                    }
                    val deferredTournamentLogos = response.data.groupBy { it.tournament }.keys.map { tournament ->
                        async {
                            val imageBitmap = repo.getTournamentLogoById(tournament.id)
                            if (imageBitmap != null)
                                tournament.logo = imageBitmap
                        }
                    }
                    deferredTeamLogos.awaitAll()
                    deferredTournamentLogos.awaitAll()
                    // after all logos are fetched update again
                    _events.value = response.data
                }
            }
        }
    }


}

