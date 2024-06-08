package com.example.minisofascore.ui.leagues

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascore.data.models.Event
import com.example.minisofascore.data.models.Sport
import com.example.minisofascore.data.models.SportType
import com.example.minisofascore.data.models.Tournament
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LeaguesViewModel : ViewModel() {

    private val repo = Repository()

    private val _leagues = MutableLiveData<List<Tournament>>()
    val leagues: LiveData<List<Tournament>> = _leagues

    var selectedSport = SportType.FOOTBALL

    private var currentlyLoadingJob: Job? = null

    fun getLeaguesBySport(sportType: SportType = selectedSport) {
        currentlyLoadingJob?.cancel()
        currentlyLoadingJob = viewModelScope.launch {
            when(val response = repo.getLeaguesBySport(sportType)) {
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