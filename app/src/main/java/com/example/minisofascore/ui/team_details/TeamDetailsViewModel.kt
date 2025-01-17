package com.example.minisofascore.ui.team_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minisofascore.data.models.TeamDetails
import com.example.minisofascore.data.remote.Result
import com.example.minisofascore.data.repository.TeamRepository
import kotlinx.coroutines.launch

class TeamDetailsViewModel : ViewModel() {


    private val _teamDetails = MutableLiveData<TeamDetails>()
    val teamDetails: LiveData<TeamDetails> = _teamDetails

    fun getTeamDetails(teamId: Int) {
        viewModelScope.launch {
            when (val response = TeamRepository.getTeamDetails(teamId)) {
                is Result.Success -> _teamDetails.value = response.data
                is Result.Error -> Log.e("aaaa", "getTeamDetails: couldn't fetch team details ${response.error}")
            }
        }
    }
}