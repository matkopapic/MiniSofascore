package com.example.minisofascore.ui.tournament_standings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascore.data.models.TournamentStandings
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.Repository
import kotlinx.coroutines.launch

class TournamentStandingsViewModel : ViewModel() {

    private val repo = Repository()

    private val _standings = MutableLiveData<TournamentStandings>()
    val standings: LiveData<TournamentStandings> = _standings

    fun getStandingsForTournament(tournamentId: Int) = viewModelScope.launch {
        when (val response = repo.getStandingsForTournament(tournamentId)) {
            is Result.Success -> _standings.value = response.data[0]
            is Result.Error -> Log.e("aaaa", "Error retrieving tournament standings : ${response.error}")
        }
    }
}