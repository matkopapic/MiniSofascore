package com.example.minisofascore.ui.leagues

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascore.data.models.SportType
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.EventRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LeaguesViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {


    private val _leagues = MutableLiveData<List<Tournament>>()
    val leagues: LiveData<List<Tournament>> = _leagues

    var selectedSport = SportType.FOOTBALL

    private var currentlyLoadingJob: Job? = null

    init {
        val sportType: SportType? = savedStateHandle[LeaguesFragment.SPORT_TYPE]
        sportType?.let {
            getLeaguesBySport(sportType)
        } ?: getLeaguesBySport()
    }

    fun getLeaguesBySport(sportType: SportType = selectedSport) {
        selectedSport = sportType
        currentlyLoadingJob?.cancel()
        currentlyLoadingJob = viewModelScope.launch {
            when(val response = EventRepository.getLeaguesBySport(sportType)) {
                is Result.Error -> Log.e("aaaa", "error:${response.error}")
                is Result.Success -> _leagues.value = response.data
            }
        }
    }

    fun cancelCurrentLoading() {
        currentlyLoadingJob?.cancel()
        currentlyLoadingJob = null
    }
}